package com.cluting.clutingbackend.evaluation.service;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.clubuser.repository.ClubUserRepository;
import com.cluting.clutingbackend.evaluation.dto.request.InterviewQuestionSaveRequestDto;
import com.cluting.clutingbackend.evaluation.dto.response.InterviewPrepResponseDto;
import com.cluting.clutingbackend.global.enums.EvalType;
import com.cluting.clutingbackend.global.enums.EvaluateStatus;
import com.cluting.clutingbackend.global.enums.QuestionType2;
import com.cluting.clutingbackend.interview.domain.Interview;
import com.cluting.clutingbackend.interview.domain.InterviewEvaluator;
import com.cluting.clutingbackend.interview.domain.InterviewQuestion;
import com.cluting.clutingbackend.interview.repository.InterviewCriteriaRepository;
import com.cluting.clutingbackend.interview.repository.InterviewEvaluatorRepository;
import com.cluting.clutingbackend.interview.repository.InterviewQuestionRepository;
import com.cluting.clutingbackend.interview.repository.InterviewRepository;
import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.plan.repository.GroupRepository;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.recruit.dto.response.RecruitNumResponseDto;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InterviewEvaluationService {
    private final RecruitRepository recruitRepository;
    private final GroupRepository groupRepository;
    private final ApplicationRepository applicationRepository;
    private final ClubUserRepository clubUserRepository;

    private final InterviewRepository interviewRepository;
    private final InterviewEvaluatorRepository interviewEvaluatorRepository;
    private final InterviewCriteriaRepository interviewCriteriaRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;

    // 면접 가능 일정 리스트 조회
//    public List<InterviewAvailableResponseDto> findAvailable(Long recruitId) {
//    }

    // 면접 일정 저장

    // 파트 존재 여부 조회
    @Transactional(readOnly = true)
    public Boolean isCommon(Long recruitId) {
        return groupRepository.findByRecruitId(recruitId).get(0).isCommon();
    }

    // 서류 합격자 수 조회
    @Transactional(readOnly = true)
    public RecruitNumResponseDto findDocRecruit(Long recruitId) {
        Map<String, Integer> groupMap = new HashMap<>();
        List<Group> groups = groupRepository.findByRecruitId(recruitId);
        int totalNum = 0;
        for (Group group : groups) {
            totalNum += group.getNumRecruit();
            if (!group.isCommon()) {
                groupMap.put(group.getName(), group.getNumDoc());
            }
        }

        return new RecruitNumResponseDto(totalNum, groupMap);
    }

    // 서류 합격자들 모두 조회하기
    @Transactional(readOnly = true)
    public List<InterviewPrepResponseDto> findApplicants(Long recruitId) {
        return applicationRepository.findByRecruitId(recruitId)
                .stream()
                .filter(application -> application.getState() == EvaluateStatus.PASS)
                .map(InterviewPrepResponseDto::toDto)
                .toList();
    }

    // 면접자들에 대한 면접 질문 저장
    @Transactional
    public void saveInterviewQuestions(Long recruitId, InterviewQuestionSaveRequestDto interviewQuestionSaveRequestDto) {
        Recruit recruit = recruitRepository.findById(recruitId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 모집공고 입니다.")
        );

        // 1. 그룹 확인 및 생성
        List<Group> groups = groupRepository.findByRecruitId(recruitId);
        Group group = groups.get(0);

        if (group.isCommon()) { // 공통 그룹일 경우
            for (InterviewQuestionSaveRequestDto.InterviewStaffAllocate allocate : interviewQuestionSaveRequestDto.getAllocates()) {
                groupRepository.save(Group.of(recruit, allocate.getGroupName(), group.getNumDoc(), group.getNumFinal(), group.getNumRecruit(), group.getWarning(), EvalType.INTERVIEW, true));
            }
        } else { // 개별 그룹일 경우
            for (Group docGroup : groups) {
                groupRepository.save(Group.of(recruit, docGroup.getName(), docGroup.getNumDoc(), docGroup.getNumFinal(), docGroup.getNumRecruit(), docGroup.getWarning(), EvalType.INTERVIEW, false));
            }
        }

        // 2. 서류 합격자 필터링 및 면접 생성
        List<Application> applications = applicationRepository.findByRecruitId(recruitId).stream()
                .filter(application -> application.getState() == EvaluateStatus.PASS)
                .toList();

        List<Interview> interviews = new ArrayList<>();
        for (Application application : applications) {
            interviews.add(interviewRepository.save(Interview.of(application)));
        }

        // 3. INTERVIEW 그룹 필터링
        groups = groupRepository.findByRecruitId(recruitId)
                .stream()
                .filter(g -> g.getEvalType() == EvalType.INTERVIEW)
                .toList();

        // 4. InterviewEvaluator 생성 및 저장
        List<InterviewEvaluator> interviewEvaluators = new ArrayList<>();
        for (InterviewQuestionSaveRequestDto.InterviewStaffAllocate allocate : interviewQuestionSaveRequestDto.getAllocates()) {
            Group assignedGroup = groups.stream()
                    .filter(g -> g.getName().equals(allocate.getGroupName()))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "그룹을 찾을 수 없습니다: " + allocate.getGroupName()));

            for (int i = 0; i < allocate.getStaff().size(); i++) {
                Long staffId = allocate.getStaff().get(i);
                ClubUser clubUser = findClubUser(staffId);
                Interview interview = interviews.get(i % interviews.size());

                InterviewEvaluator interviewEvaluator = InterviewEvaluator.of(clubUser, interview, assignedGroup);
                interviewEvaluators.add(interviewEvaluatorRepository.save(interviewEvaluator));
            }
        }

        // 5. 공통 질문 저장
        if (interviewQuestionSaveRequestDto.getCommon() != null) {
            for (String question : interviewQuestionSaveRequestDto.getCommon()) {
                interviewQuestionRepository.save(
                        InterviewQuestion.of(null, null, question, QuestionType2.COMMON) // 평가자와 면접 없음
                );
            }
        }

        // 6. 그룹 질문 저장
        if (interviewQuestionSaveRequestDto.getGroup() != null) {
            for (InterviewQuestionSaveRequestDto.InterviewGroupQuestion groupQuestion : interviewQuestionSaveRequestDto.getGroup()) {
                Group targetGroup = groups.stream()
                        .filter(g -> g.getName().equals(groupQuestion.getGroupName()))
                        .findFirst()
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "그룹을 찾을 수 없습니다: " + groupQuestion.getGroupName()));

                for (String question : groupQuestion.getQuestion()) {
                    // Group에 해당하는 InterviewEvaluator 찾기
                    InterviewEvaluator evaluator = interviewEvaluatorRepository.findByGroupId(targetGroup.getId());

                    interviewQuestionRepository.save(
                            InterviewQuestion.of(evaluator, null, question, QuestionType2.COMMON)
                    );
                }
            }
        }

        // 7. 개인 질문 저장
        if (interviewQuestionSaveRequestDto.getIndividual() != null) {
            for (InterviewQuestionSaveRequestDto.InterviewIndividualQuestion individualQuestion : interviewQuestionSaveRequestDto.getIndividual()) {
                Interview targetInterview = interviews.stream()
                        .filter(interview -> interview.getApplication().getUser().getName().equals(individualQuestion.getName()) &&
                                interview.getApplication().getUser().getPhone().equals(individualQuestion.getPhone()))
                        .findFirst()
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "대상을 찾을 수 없습니다: " + individualQuestion.getName()));

                InterviewEvaluator evaluator = interviewEvaluatorRepository.findByInterviewId(targetInterview.getId());

                for (String question : individualQuestion.getQuestion()) {
                    interviewQuestionRepository.save(
                            InterviewQuestion.of(evaluator, targetInterview, question, QuestionType2.PERSONAL)
                    );
                }
            }
        }

    }

    public ClubUser findClubUser(Long clubUserId) {
        return clubUserRepository.findById(clubUserId).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "존재하지 않는 운영진 입니다."
                )
        );
    }
}
