package com.cluting.clutingbackend.evaluation.service;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.evaluation.dto.GroupResponse;
import com.cluting.clutingbackend.evaluation.dto.interview.*;
import com.cluting.clutingbackend.global.enums.CurrentStage;
import com.cluting.clutingbackend.global.enums.EvaluateStatus;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.interview.domain.Interview;
import com.cluting.clutingbackend.interview.domain.InterviewEvaluator;
import com.cluting.clutingbackend.interview.repository.InterviewEvaluatorRepository;
import com.cluting.clutingbackend.interview.repository.InterviewRepository;
import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.plan.repository.DocumentEvaluatorRepository;
import com.cluting.clutingbackend.plan.repository.GroupRepository;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewEvaluationService {

    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;
    private final InterviewEvaluatorRepository interviewEvaluatorRepository;
    private final DocumentEvaluatorRepository documentEvaluatorRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final RecruitRepository recruitRepository;

    public List<InterviewEvaluationResponse> getInterviewEvaluations(Long recruitId, CustomUserDetails currentUser, InterviewEvaluationRequest request) {
        Long currentClubUserId = currentUser.getUser().getId();

        // Step 1: 모집 공고 ID로 Application 조회
        List<Application> applications = applicationRepository.findByRecruitId(recruitId);

        // Step 2: Application ID로 Interview 조회
        List<Interview> interviews = interviewRepository.findByApplicationIdIn(
                applications.stream().map(Application::getId).collect(Collectors.toList()));

        // Step 3: Interview ID로 InterviewEvaluator 조회 및 단계별 분리
        List<InterviewEvaluator> evaluators = interviewEvaluatorRepository.findByInterviewIdIn(
                interviews.stream().map(Interview::getId).collect(Collectors.toList()));

        return evaluators.stream()
                .filter(evaluator -> evaluator.getClubUser() != null
                        && evaluator.getClubUser().getUser().getId().equals(currentClubUserId)
                        && (request.getGroupName() == null ||
                        (evaluator.getGroup() != null &&
                                evaluator.getGroup().getName().equals(request.getGroupName()))))
                .map(evaluator -> mapToResponse(evaluator, request.getSortOrder()))
                .sorted((resp1, resp2) -> sortResponses(resp1, resp2, request.getSortOrder()))
                .collect(Collectors.toList());
    }

    private InterviewEvaluationResponse mapToResponse(InterviewEvaluator evaluator, String sortOrder) {
        Interview interview = evaluator.getInterview();
        Application application = interview.getApplication();
        User applicant = application.getUser();

        int totalEvaluators = interviewEvaluatorRepository.countDistinctByInterviewId(interview.getId());
        int currentEvaluators = interview.getNumClubUser() != null ? interview.getNumClubUser() : 0;

        return InterviewEvaluationResponse.builder()
                .stage(evaluator.getStage().name())
                .applicantName(applicant.getName())
                .applicantPhone(applicant.getPhone())
                .groupName(evaluator.getGroup() != null ? evaluator.getGroup().getName() : "N/A")
                .evaluationStatus(currentEvaluators + "/" + totalEvaluators)
                .build();
    }

    private int sortResponses(InterviewEvaluationResponse resp1, InterviewEvaluationResponse resp2, String sortOrder) {
        if ("newest".equals(sortOrder)) {
            return resp2.getStage().compareTo(resp1.getStage());
        } else if ("oldest".equals(sortOrder)) {
            return resp1.getStage().compareTo(resp2.getStage());
        }
        return 0;
    }


    public List<GroupResponse> getGroupsByRecruitId(Long recruitId) {
        // Group 엔티티에서 recruitId로 그룹 조회
        List<Group> groups = groupRepository.findAllByRecruitId(recruitId);

        // GroupResponse로 매핑
        return groups.stream()
                .map(group -> new GroupResponse(group.getId(), group.getName()))
                .toList();
    }

    public List<InterviewEvaluationResultDto> evaluateInterviews(Long recruitId, CustomUserDetails currentUser) {
        // 해당 Recruit에 연결된 Application 가져오기
        List<Application> applications = applicationRepository.findByRecruitId(recruitId);

        // Application을 기준으로 Interview 조회
        List<Interview> interviews = interviewRepository.findByApplicationIn(applications);

        // Interview 정렬 및 평가 상태 업데이트
        List<Interview> sortedInterviews = interviews.stream()
                .sorted(Comparator.comparingInt(Interview::getScore).reversed())
                .toList();

        Recruit recruit = applications.get(0).getRecruit(); // 같은 Recruit에 속하므로 하나 가져옴
        int numDoc = recruit.getNumDoc(); // 서류 합격 인원 수

        List<InterviewEvaluationResultDto> results = new ArrayList<>();
        for (int i = 0; i < sortedInterviews.size(); i++) {
            Interview interview = sortedInterviews.get(i);
            if (i < numDoc) {
                interview.setState(EvaluateStatus.PASS);
            } else {
                interview.setState(EvaluateStatus.FAIL);
            }
            results.add(
                    InterviewEvaluationResultDto.builder()
                            .interviewId(interview.getId())
                            .state(interview.getState())
                            .score(interview.getScore())
                            .build()
            );
        }

        interviewRepository.saveAll(sortedInterviews); // 변경 사항 저장
        return results;
    }

    public Map<String, List<InterviewEvaluationCompleteResponse>> getCompletedEvaluations(Long recruitId) {
        // 1. recruitId로 Application 목록 찾기
        List<Application> applications = applicationRepository.findByRecruitId(recruitId);

        // 2. 합격(PASS)과 불합격(FAIL) 상태로 Interview 분리
        List<Interview> interviews = interviewRepository.findByApplicationIn(applications);
        List<InterviewEvaluationCompleteResponse> passedEvaluations = new ArrayList<>();
        List<InterviewEvaluationCompleteResponse> failedEvaluations = new ArrayList<>();

        for (Interview interview : interviews) {
            // 3. Interview의 상태에 따라 처리
            if (EvaluateStatus.PASS.equals(interview.getState())) {
                passedEvaluations.add(mapToEvaluationResponse(interview));
            } else if (EvaluateStatus.FAIL.equals(interview.getState())) {
                failedEvaluations.add(mapToEvaluationResponse(interview));
            }
        }

        // 4. 결과 맵핑
        Map<String, List<InterviewEvaluationCompleteResponse>> result = new HashMap<>();
        result.put("PASS", passedEvaluations);
        result.put("FAIL", failedEvaluations);

        return result;
    }

    private InterviewEvaluationCompleteResponse mapToEvaluationResponse(Interview interview) {
        // 지원자 정보 가져오기
        Application application = interview.getApplication();
        User user = application.getUser();

        // 그룹명 가져오기
        List<InterviewEvaluator> evaluators = interviewEvaluatorRepository.findByInterview(interview);

        if (evaluators.isEmpty()) {
            throw new RuntimeException("InterviewEvaluator not found for interview");
        }

        InterviewEvaluator evaluator = evaluators.get(0);

        Group group = evaluator.getGroup();
        if (group == null) {
            throw new RuntimeException("Group not found for InterviewEvaluator");
        }

        return new InterviewEvaluationCompleteResponse(
                interview.getState().name(),
                user.getName(),
                user.getPhone(),
                group.getName()
        );
    }

    @Transactional
    public void completeInterviewEvaluation(Long recruitId, List<InterviewEvaluationCompleteRequest> evaluations, CustomUserDetails currentUser) {
        // 1. 리크루팅이 존재하는지 확인
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new IllegalArgumentException("리크루팅이 존재하지 않습니다."));

        // 2. 각 면접 평가 처리
        for (InterviewEvaluationCompleteRequest evaluation : evaluations) {
            Interview interview = interviewRepository.findById(evaluation.getInterviewId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 면접을 찾을 수 없습니다."));

            // 3. 면접 상태 업데이트
            EvaluateStatus status = EvaluateStatus.valueOf(evaluation.getState().toUpperCase());
            interview.setState(status);
            interviewRepository.save(interview); // 면접 상태 저장

            // 4. 지원서 상태 업데이트
            Application application = interview.getApplication();
            application.setState(status);
            applicationRepository.save(application); // 지원서 상태 저장
        }

        // 5. 리크루팅의 currentStage를 FINAL_PASS로 업데이트
        recruit.setCurrentStage(CurrentStage.FINAL_PASS);
        recruitRepository.save(recruit); // 리크루팅 상태 저장
    }

    public EvaluateUserResponse completeEachInterviewEvaluation(Long recruitId, InterviewEvaluationCompleteRequest request) {
        // 면접 ID를 통해 면접 정보 가져오기
        Interview interview = interviewRepository.findById(request.getInterviewId())
                .orElseThrow(() -> new IllegalArgumentException("해당 면접을 찾을 수 없습니다."));

        // 면접 평가 결과 업데이트 (PASS 또는 FAIL)
        if ("PASS".equalsIgnoreCase(request.getState())) {
            interview.setState(EvaluateStatus.PASS);
        } else if ("FAIL".equalsIgnoreCase(request.getState())) {
            interview.setState(EvaluateStatus.FAIL);
        } else {
            throw new IllegalArgumentException("결과 값은 'PASS' 또는 'FAIL'이어야 합니다.");
        }
        interviewRepository.save(interview); // 면접 상태 저장

        // 해당 면접을 진행한 지원자 정보 가져오기
        Application application = interview.getApplication();
        User user = application.getUser();

        // 그룹명 가져오기 (InterviewEvaluator를 통해)
        InterviewEvaluator evaluator = interviewEvaluatorRepository.findFirstByInterviewId(request.getInterviewId())
                .orElseThrow(() -> new IllegalArgumentException("해당 면접의 평가자가 없습니다."));
        Group group = evaluator.getGroup();

        // 응답 객체 생성
        return new EvaluateUserResponse(
                user.getName(),
                user.getPhone(),
                group != null ? group.getName() : null,
                request.getState()
        );
    }

    public void updateStateToObjection(Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 면접을 찾을 수 없습니다."));
        interview.setState(EvaluateStatus.OBJECTION);
        interviewRepository.save(interview);
    }
}
