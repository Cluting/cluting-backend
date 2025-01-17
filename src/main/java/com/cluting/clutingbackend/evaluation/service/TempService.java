package com.cluting.clutingbackend.evaluation.service;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.evaluation.dto.response.EvaluationResponse;
import com.cluting.clutingbackend.global.enums.EvaluateStatus;
import com.cluting.clutingbackend.global.enums.Stage;
import com.cluting.clutingbackend.global.exception.CustomException;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.interview.domain.Interview;
import com.cluting.clutingbackend.interview.domain.InterviewEvaluator;
import com.cluting.clutingbackend.interview.repository.InterviewEvaluatorRepository;
import com.cluting.clutingbackend.interview.repository.InterviewRepository;
import com.cluting.clutingbackend.plan.domain.DocumentEvaluator;
import com.cluting.clutingbackend.plan.repository.DocumentEvaluatorRepository;
import com.cluting.clutingbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.cluting.clutingbackend.global.exception.ErrorCode.APP_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TempService {

    private final ApplicationRepository applicationRepository;
    private final DocumentEvaluatorRepository documentEvaluatorRepository;
    private final InterviewEvaluatorRepository interviewEvaluatorRepository;
    private final InterviewRepository interviewRepository;


    public List<EvaluationResponse> getEvaluationsByStage(
            Long recruitId, CustomUserDetails currentUser, String stage) {

        List<Application> applications = applicationRepository.findByRecruitId(recruitId);

        return applications.stream()
                .filter(application -> {
                    List<DocumentEvaluator> evaluators = documentEvaluatorRepository.findByApplicationId(application.getId());

                    switch (stage) {
                        case "BEFORE":
                            // 평가 전: 계정 주인이 평가 전 상태인 경우
                            return evaluators.stream().anyMatch(evaluator ->
                                    evaluator.getClubUser().getUser().getId().equals(currentUser.getId()) &&
                                            evaluator.getStage() == Stage.BEFORE);

                        case "ING":
                            // 평가 중:
                            boolean isCurrentUserInProgress = evaluators.stream().anyMatch(evaluator ->
                                    evaluator.getClubUser().getUser().getId().equals(currentUser.getId()) &&
                                            evaluator.getStage() == Stage.ING);

                            boolean isTeamInProgress = evaluators.stream().anyMatch(evaluator ->
                                    !evaluator.getClubUser().getUser().getId().equals(currentUser.getId()) &&
                                            (evaluator.getStage() == Stage.ING || evaluator.getStage() == Stage.BEFORE));

                            return isCurrentUserInProgress || isTeamInProgress;

                        case "AFTER":
                            // 평가 후: 모든 운영진이 평가를 완료한 경우
                            return evaluators.stream().allMatch(evaluator ->
                                    evaluator.getStage() == Stage.AFTER);

                        case "COMPLETE":
                            // 평가 완료: 평가 후에 합격/불합격 결정이 내려진 경우
                            return application.isCompleted();

                        default:
                            return false;
                    }
                })
                .map(application -> mapToResponseWithDoc(application, currentUser))
                .collect(Collectors.toList());
    }


    private EvaluationResponse mapToResponseWithDoc(
            Application application,
            CustomUserDetails currentUser
    ) {
        User user = application.getUser();
        List<DocumentEvaluator> evaluators = documentEvaluatorRepository.findByApplicationId(application.getId());
    
        // 현재 로그인한 유저의 상태 확인
        EvaluationResponse.EvaluatorInfo currentEvaluator = evaluators.stream()
                .filter(evaluator -> evaluator.getClubUser().getUser().getId().equals(currentUser.getId()))
                .map(evaluator -> new EvaluationResponse.EvaluatorInfo(
                        evaluator.getClubUser().getUser().getName(),
                        evaluator.getStage()
                ))
                .findFirst()
                .orElse(null);
    
        // 다른 운영진 정보 추출
        List<EvaluationResponse.EvaluatorInfo> otherEvaluators = evaluators.stream()
                .filter(evaluator -> !evaluator.getClubUser().getUser().getId().equals(currentUser.getId()))
                .map(evaluator -> new EvaluationResponse.EvaluatorInfo(
                        evaluator.getClubUser().getUser().getName(),
                        evaluator.getStage()
                ))
                .collect(Collectors.toList());
    
        // 모든 운영진이 평가 완료 상태인지 확인
        boolean isAllEvaluatorsAfter = evaluators.stream()
                .allMatch(evaluator -> evaluator.getStage() == Stage.AFTER);
    
        // 평가 결과 결정 여부 확인
        boolean isFinalDecisionMade = application.isCompleted();
    
        // 평가 상태 결정
        Stage evaluationStage;
    
        if (isFinalDecisionMade) {
            // 평가 완료
            evaluationStage = Stage.AFTER;
        } else if (isAllEvaluatorsAfter) {
            // 모든 운영진이 평가 완료 상태 (최종 판단은 안 됨)
            evaluationStage = Stage.EDITABLE;
        } else if (currentEvaluator != null && currentEvaluator.getStage().equals("ING")) {
            // 현재 유저가 평가 중
            evaluationStage = Stage.ING;
        } else if (currentEvaluator == null) {
            // 현재 유저가 평가자로 지정되지 않은 경우
            evaluationStage = Stage.READABLE;
        } else {
            // 평가 전
            evaluationStage = Stage.BEFORE;
        }
    
        // 응답 생성
        return new EvaluationResponse(
                application.getId(),
                evaluationStage,
                user.getName(),
                user.getPhone(),
                application.getRecruit_group(),
                application.getNumClubUser() + "/" + evaluators.size(),
                application.getCreatedAt(),
                currentEvaluator,
                otherEvaluators
        );
    }


    public void updateEvaluateStatus(Long applicationId, EvaluateStatus newStatus) {
        // Application 찾기
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new CustomException(APP_NOT_FOUND,"Application not found with ID: " + applicationId));

        // EvaluateStatus 업데이트
        application.setState(newStatus);

        // 업데이트된 Application 저장
        applicationRepository.save(application);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<EvaluationResponse> getEvaluationsByStage(
            Long recruitId,
            String groupName,
            String sortOrder,
            Stage stage,
            CustomUserDetails currentUser) {

        // Step 1: 모집 공고 ID로 Application 조회
        List<Application> applications = applicationRepository.findByRecruitId(recruitId);
        if (applications == null || applications.isEmpty()) {
            return Collections.emptyList(); // Application이 없으면 빈 리스트 반환
        }

        // Step 2: Application ID로 Interview 조회
        List<Interview> interviews = interviewRepository.findByApplicationIdIn(
                applications.stream().map(Application::getId).collect(Collectors.toList()));
        if (interviews == null || interviews.isEmpty()) {
            return Collections.emptyList(); // Interview가 없으면 빈 리스트 반환
        }

        // Step 3: Interview ID로 InterviewEvaluator 조회 및 단계별 분리
        List<InterviewEvaluator> evaluators = interviewEvaluatorRepository.findByInterviewIdIn(
                interviews.stream().map(Interview::getId).collect(Collectors.toList()));
        if (evaluators == null || evaluators.isEmpty()) {
            return Collections.emptyList(); // Evaluators가 없으면 빈 리스트 반환
        }

        ////////////////////////////
        // 그룹명 필터링
        if (groupName != null) {
            evaluators = evaluators.stream()
                    .filter(evaluator -> evaluator.getGroup() != null
                            && evaluator.getGroup().getName().equals(groupName))
                    .collect(Collectors.toList());
        }

        // 평가 상태 필터링 (현재 유저 기준)
        evaluators = evaluators.stream()
                .filter(evaluator -> {
                    switch (stage) {
                        case BEFORE: // 평가 전
                            return evaluator.getClubUser().getUser().getId().equals(currentUser.getId())
                                    && evaluator.getStage() == Stage.BEFORE;

                        case ING: // 평가 중
                            return evaluator.getClubUser().getUser().getId().equals(currentUser.getId())
                                    && evaluator.getStage() == Stage.ING;

                        case AFTER: // 평가 후
                            return evaluator.getStage() == Stage.AFTER;

                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());

        // 정렬 로직
        if ("newest".equals(sortOrder)) {
            evaluators.sort(Comparator.comparing(InterviewEvaluator::getInterviewTime, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
        } else if ("oldest".equals(sortOrder)) {
            evaluators.sort(Comparator.comparing(InterviewEvaluator::getInterviewTime, Comparator.nullsLast(Comparator.naturalOrder())));
        }

        // EvaluationResponse로 매핑
        List<InterviewEvaluator> finalEvaluators = evaluators;

        return evaluators.stream()
                .map(evaluator -> mapToResponseWithInterview(evaluator, finalEvaluators))
                .collect(Collectors.toList());
    }

    private EvaluationResponse mapToResponseWithInterview(InterviewEvaluator evaluator, List<InterviewEvaluator> evaluators) {
        Application application = evaluator.getInterview().getApplication();
        User applicant = application.getUser();

        // 현재 로그인한 평가자 정보 추출
        EvaluationResponse.EvaluatorInfo currentEvaluator = evaluators.stream()
                .filter(e -> e.getClubUser() != null && e.getClubUser().getUser() != null
                        && e.getClubUser().getUser().getId().equals(evaluator.getClubUser().getUser().getId()))
                .map(e -> new EvaluationResponse.EvaluatorInfo(
                        e.getClubUser().getUser().getName(),
                        e.getStage()
                ))
                .findFirst()
                .orElse(null);

        // 다른 운영진 정보 추출
        List<EvaluationResponse.EvaluatorInfo> otherEvaluators = evaluators.stream()
                .filter(e -> e.getClubUser() != null && e.getClubUser().getUser() != null
                        && !e.getClubUser().getUser().getId().equals(evaluator.getClubUser().getUser().getId()))
                .map(e -> new EvaluationResponse.EvaluatorInfo(
                        e.getClubUser().getUser().getName(),
                        e.getStage()
                ))
                .toList();

        // 모든 운영진이 평가 완료 상태인지 확인
        boolean isAllEvaluatorsAfter = evaluators.stream()
                .allMatch(e -> e.getStage().equals(Stage.AFTER));

        // 평가 결과 결정 여부 확인
        boolean isFinalDecisionMade = application.isCompleted();

        // 평가 상태 결정
        Stage evaluationStage;
        if (isFinalDecisionMade) {
            evaluationStage = Stage.AFTER;
        } else if (isAllEvaluatorsAfter) {
            evaluationStage = Stage.EDITABLE;
        } else if (currentEvaluator != null && currentEvaluator.getStage().equals(Stage.ING)) {
            evaluationStage = Stage.ING;
        } else if (currentEvaluator == null) {
            evaluationStage = Stage.READABLE;
        } else {
            evaluationStage = Stage.BEFORE;
        }

        // 응답 객체 생성
        return new EvaluationResponse(
                application.getId(),
                evaluationStage,
                applicant.getName(),
                applicant.getPhone(),
                evaluator.getGroup() != null ? evaluator.getGroup().getName() : null,
                evaluators.size() + "/" + evaluators.size(),
                application.getCreatedAt(),
                currentEvaluator,
                otherEvaluators
        );
    }
    private int sortResponses(EvaluationResponse resp1, EvaluationResponse resp2, String sortOrder) {
        if ("newest".equals(sortOrder)) {
            return resp2.getEvaluationStage().compareTo(resp1.getEvaluationStage());
        } else if ("oldest".equals(sortOrder)) {
            return resp1.getEvaluationStage().compareTo(resp2.getEvaluationStage());
        }
        return 0;
    }

}

