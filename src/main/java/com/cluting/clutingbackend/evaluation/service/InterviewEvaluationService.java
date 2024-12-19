package com.cluting.clutingbackend.evaluation.service;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.clubuser.repository.ClubUserRepository;
import com.cluting.clutingbackend.evaluation.dto.GroupResponse;
import com.cluting.clutingbackend.evaluation.dto.document.ApplicantInfo;
import com.cluting.clutingbackend.evaluation.dto.interview.*;
import com.cluting.clutingbackend.global.enums.CurrentStage;
import com.cluting.clutingbackend.global.enums.EvaluateStatus;
import com.cluting.clutingbackend.global.enums.QuestionType2;
import com.cluting.clutingbackend.global.enums.Stage;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.interview.domain.*;
import com.cluting.clutingbackend.interview.repository.*;
import com.cluting.clutingbackend.plan.domain.DocumentEvaluator;
import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.plan.domain.TalentProfile;
import com.cluting.clutingbackend.plan.repository.GroupRepository;
import com.cluting.clutingbackend.plan.repository.TalentProfileRepository;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewEvaluationService {

    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;
    private final InterviewEvaluatorRepository interviewEvaluatorRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ClubUserRepository clubUserRepository;
    private final RecruitRepository recruitRepository;
    private final TalentProfileRepository talentProfileRepository;
    private final InterviewScoreRepository interviewScoreRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final InterviewCriteriaRepository interviewCriteriaRepository;

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
        List<Group> groups = groupRepository.findAllByRecruitIdForInterview(recruitId);

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

    private static String getString(List<DocumentEvaluator> evaluators) {
        String groupName = null;
        if(evaluators.get(0).getGroup().isCommon()){ //공통그룹이면
            for (DocumentEvaluator evaluator : evaluators){
                if (evaluator.getGroup().getEvalType().toString().equals("DOCUMENT")){
                    groupName = evaluators.get(0).getGroup().getName();
                    System.out.println("@@----\n\n그룹명 = "+evaluator.getGroup().getEvalType()+"\n\n");
                }
                else{
                    groupName = "";
                }
            }
        }
        else {
            groupName = evaluators.get(0).getGroup().getName(); // 첫 번째 평가자의 그룹 사용
        }
        return groupName;
    }

    public EachInterviewEvaluationResponse getInterviewEvaluation(Long recruitId, Long interviewId, CustomUserDetails currentUser) {
        // 1. 지원자 정보
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new DocumentEvaluationService.ResourceNotFoundException("Interview not found"));
        Application application = interview.getApplication();
        User user = application.getUser();

        List<InterviewEvaluator> evaluators = interviewEvaluatorRepository.findByInterviewId(interviewId);
        String groupName = evaluators.isEmpty() ? null : evaluators.get(0).getGroup().getName();

        ApplicantInfo applicantInfo = ApplicantInfo.of(
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getLocation(),
                user.getProfile(),
                user.getSchool(),
                user.getMajor(),
                user.getDoubleMajor(),
                String.valueOf(user.getSemester()),
                groupName
        );

        // 2. 면접 질문 및 답변
        List<InterviewQuestion> interviewQuestions = interviewQuestionRepository.findByInterviewId(interviewId);
        Map<QuestionType2, List<InterviewQA>> groupedQuestions = new HashMap<>();
        for (QuestionType2 type : QuestionType2.values()) {
            groupedQuestions.put(type, new ArrayList<>());
        }

        for (InterviewQuestion question : interviewQuestions) {
            InterviewAnswer answer = question.getInterviewAnswer();
            groupedQuestions.get(question.getType()).add(InterviewQA.of(
                    question.getContent(),
                    answer == null ? null : answer.getContent()
            ));
        }

        // 3. 인재상
        List<TalentProfile> talentProfiles = talentProfileRepository.findByGroupId(evaluators.isEmpty() ? null : evaluators.get(0).getGroup().getId());
        List<String> talentProfileDetails = talentProfiles.stream()
                .map(TalentProfile::getProfile)
                .collect(Collectors.toList());

        // 4. 총점 평균
        Integer averageScore = interview.getScore();

        // 5. 다른 운영진 평가 보기
        List<InterviewEvaluatorScores> evaluatorScores = evaluators.stream()
                .map(evaluator -> InterviewEvaluatorScores.of(evaluator, interviewScoreRepository))
                .toList();

        // 6. 내 평가 보기
        Long currentClubUserId = currentUser.getUser().getId();
        ClubUser currentClubUser = clubUserRepository.findById(currentClubUserId)
                .orElseThrow(() -> new DocumentEvaluationService.ResourceNotFoundException("Club User not found"));
        InterviewEvaluatorScores myEvaluation = evaluators.stream()
                .filter(evaluator -> evaluator.getClubUser().getId().equals(currentClubUserId))
                .findFirst()
                .map(evaluator -> InterviewEvaluatorScores.of(evaluator, interviewScoreRepository))
                .orElse(null);

        return new EachInterviewEvaluationResponse(
                applicantInfo,
                groupedQuestions,
                talentProfileDetails,
                averageScore,
                evaluatorScores,
                myEvaluation
        );
    }

    @Transactional
    public InterviewEvaluationResponseDto evaluateInterview(Long interviewId, Long clubUserId, InterviewEvaluationRequestDto request) {
        // 면접 평가자 찾기
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid interview ID"));

        // 면접 평가자(InterviewEvaluator) 찾기
        InterviewEvaluator evaluator = interviewEvaluatorRepository.findByInterviewIdAndClubUserId(interviewId, clubUserId)
                .orElseThrow(() -> new IllegalArgumentException("Evaluator not found"));

        // 평가 기준 처리
        int totalScore = 0;
        for (InterviewEvaluationRequestDto.CriteriaEvaluation criteriaEvaluation : request.getCriteriaEvaluations()) {
            InterviewCriteria criteria = interviewCriteriaRepository.findById(criteriaEvaluation.getCriteriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid criteria ID"));

            InterviewScore score = new InterviewScore();
            score.setInterviewCriteria(criteria);
            score.setInterviewEvaluator(evaluator);
            score.setScore(criteriaEvaluation.getScore());
            totalScore += criteriaEvaluation.getScore();

            interviewScoreRepository.save(score);
        }

        // 평가자의 총 점수 업데이트
        evaluator.setScore(totalScore);
        evaluator.setComment(request.getComment());
        interviewEvaluatorRepository.save(evaluator);

        // 면접 엔티티의 numClubUser 업데이트 (null이면 1로 설정, 아니면 증가)
        if (interview.getNumClubUser() == null) {
            interview.setNumClubUser(1);  // 처음 평가하는 경우 numClubUser가 null일 수 있으므로 1로 설정
        } else {
            interview.setNumClubUser(interview.getNumClubUser() + 1);  // 기존 값에 1을 더함
        }

        // 새로운 평균 점수 계산
        int newAverageScore = interview.getScore() == null ?
                totalScore : (interview.getScore() * (interview.getNumClubUser() - 1) + totalScore) / interview.getNumClubUser();
        interview.setScore(newAverageScore);

        // 면접 상태를 'EDITABLE'로 업데이트
        evaluator.setStage(Stage.EDITABLE);
        interviewEvaluatorRepository.save(evaluator);
        interviewRepository.save(interview);

        // 응답 생성
        return new InterviewEvaluationResponseDto(interviewId, totalScore, request.getComment(), "UPDATED");
    }

    public List<InterviewResponseDTO> getInterviewScheduleByRecruitId(Long recruitId) {
        // Step 1: Application 엔티티 조회
        List<Application> applications = applicationRepository.findAllByRecruitId(recruitId);
        List<Long> applicationIds = applications.stream().map(Application::getId).collect(Collectors.toList());

        // Step 2: Interview 엔티티 조회
        List<Interview> interviews = interviewRepository.findAllByApplicationIdIn(applicationIds);
        List<Long> interviewIds = interviews.stream().map(Interview::getId).collect(Collectors.toList());

        // Step 3: InterviewEvaluator 엔티티 조회
        List<InterviewEvaluator> evaluators = interviewEvaluatorRepository.findAllByInterviewIdIn(interviewIds);

        // Step 4: 날짜 및 시간대 오름차순 정렬
        List<InterviewEvaluator> sortedEvaluators = evaluators.stream()
                .sorted(Comparator.comparing(e -> e.getInterviewTime())) // 날짜 및 시간대 기준 정렬
                .collect(Collectors.toList());

        // Step 5: 날짜별, 시간대별로 데이터 그룹화
        Map<LocalDate, Map<LocalTime, List<InterviewEvaluator>>> groupedData = sortedEvaluators.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getInterviewTime().toLocalDate(),
                        Collectors.groupingBy(e -> e.getInterviewTime().toLocalTime())
                ));

        // Step 6: DTO 생성
        List<InterviewResponseDTO> response = groupedData.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // 날짜 오름차순 정렬
                .map(dateEntry -> {
                    LocalDate date = dateEntry.getKey();
                    Map<LocalTime, List<InterviewEvaluator>> timeData = dateEntry.getValue();

                    List<InterviewResponseDTO.TimeSlotDTO> timeSlots = timeData.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey()) // 시간대 오름차순 정렬
                            .map(timeEntry -> {
                                LocalTime time = timeEntry.getKey();
                                List<InterviewEvaluator> evaluatorsAtTime = timeEntry.getValue();

                                // 그룹별 데이터 분리
                                Map<Long, List<InterviewEvaluator>> groupedByGroup = evaluatorsAtTime.stream()
                                        .collect(Collectors.groupingBy(evaluator -> evaluator.getGroup().getId()));

                                List<InterviewResponseDTO.TimeSlotDTO.GroupDTO> groups = groupedByGroup.entrySet().stream()
                                        .map(groupEntry -> {
                                            Long groupId = groupEntry.getKey();
                                            List<InterviewEvaluator> groupEvaluators = groupEntry.getValue();

                                            // 면접관 이름 리스트
                                            List<Long> clubUserIds = groupEvaluators.stream()
                                                    .map(evaluator -> evaluator.getClubUser().getId())
                                                    .distinct()
                                                    .collect(Collectors.toList());
                                            List<String> evaluatorNames = clubUserRepository.findAllByIdIn(clubUserIds).stream()
                                                    .map(clubUser -> clubUser.getUser().getName())
                                                    .collect(Collectors.toList());

                                            // 면접자 이름 리스트
                                            List<Long> applicantIds = groupEvaluators.stream()
                                                    .map(e -> e.getInterview().getApplication().getUser().getId())
                                                    .distinct()
                                                    .collect(Collectors.toList());
                                            List<String> applicantNames = userRepository.findAllByIdIn(applicantIds).stream()
                                                    .map(User::getName)
                                                    .collect(Collectors.toList());

                                            return InterviewResponseDTO.TimeSlotDTO.GroupDTO.builder()
                                                    .groupName(groupEvaluators.get(0).getGroup().getName()) // 그룹 이름
                                                    .interviewer(evaluatorNames)
                                                    .interviewee(applicantNames)
                                                    .build();
                                        }).collect(Collectors.toList());

                                return InterviewResponseDTO.TimeSlotDTO.builder()
                                        .time(time.toString())
                                        .groups(groups)
                                        .build();
                            }).collect(Collectors.toList());

                    return InterviewResponseDTO.builder()
                            .date(date.toString())
                            .timeSlots(timeSlots)
                            .build();
                }).collect(Collectors.toList());

        return response;
    }


}
