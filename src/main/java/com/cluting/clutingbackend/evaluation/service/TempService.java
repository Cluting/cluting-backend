package com.cluting.clutingbackend.evaluation.service;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.evaluation.dto.response.DocumentEvaluationResponse;
import com.cluting.clutingbackend.global.enums.Stage;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.plan.domain.DocumentEvaluator;
import com.cluting.clutingbackend.plan.repository.DocumentEvaluatorRepository;
import com.cluting.clutingbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TempService {

    private final ApplicationRepository applicationRepository;
    private final DocumentEvaluatorRepository documentEvaluatorRepository;

    public List<DocumentEvaluationResponse> getEvaluationsByStage(
            Long recruitId, CustomUserDetails currentUser, String stage) {

        List<Application> applications = applicationRepository.findByRecruitId(recruitId);

        return applications.stream()
                .filter(application -> {
                    List<DocumentEvaluator> evaluators = documentEvaluatorRepository.findByApplicationId(application.getId());

                    switch (stage) {
                        case "BEFORE":
                            return evaluators.stream().anyMatch(evaluator ->
                                    evaluator.getClubUser().getId().equals(currentUser.getId()) &&
                                            evaluator.getStage().name().equals("BEFORE"));
                        case "ING":
                            boolean isCurrentUserInProgress = evaluators.stream().anyMatch(evaluator ->
                                    evaluator.getClubUser().getId().equals(currentUser.getId()) &&
                                            evaluator.getStage().name().equals("ING"));
                            boolean isTeamInProgress = evaluators.stream().anyMatch(evaluator ->
                                    !evaluator.getClubUser().getId().equals(currentUser.getId()) &&
                                            evaluator.getStage().name().equals("ING"));
                            return isCurrentUserInProgress || isTeamInProgress;
                        case "AFTER":
                            return evaluators.stream().allMatch(evaluator ->
                                    evaluator.getStage().name().equals("AFTER"));
                        case "COMPLETE":
                            return application.isFinalDecisionMade(); // 합격/불합격 여부 판단 필드
                        default:
                            return false;
                    }
                })
                .map(application -> mapToResponse(application, recruitId, currentUser))
                .collect(Collectors.toList());
    }

    private DocumentEvaluationResponse mapToResponse(
            Application application,
            Long recruitId,
            CustomUserDetails currentUser,
            String targetStage
    ) {
        User user = application.getUser();
        List<DocumentEvaluator> evaluators = documentEvaluatorRepository.findByApplicationId(application.getId());

        // 현재 로그인한 유저의 상태 확인
        DocumentEvaluationResponse.EvaluatorInfo currentEvaluator = evaluators.stream()
                .filter(evaluator -> evaluator.getClubUser().getId().equals(currentUser.getId()))
                .map(evaluator -> new DocumentEvaluationResponse.EvaluatorInfo(
                        evaluator.getClubUser().getUser().getName(),
                        evaluator.getStage().name()
                ))
                .findFirst()
                .orElse(null);

        // 다른 운영진 정보 추출
        List<DocumentEvaluationResponse.EvaluatorInfo> otherEvaluators = evaluators.stream()
                .filter(evaluator -> !evaluator.getClubUser().getId().equals(currentUser.getId()))
                .map(evaluator -> new DocumentEvaluationResponse.EvaluatorInfo(
                        evaluator.getClubUser().getUser().getName(),
                        evaluator.getStage().name()
                ))
                .collect(Collectors.toList());

        // 전체 운영진이 평가 완료인지 확인
        boolean isAllEvaluatorsCompleted = evaluators.stream()
                .allMatch(evaluator -> evaluator.getStage().name().equals("AFTER"));

        // 평가 결과 결정 여부 확인
        boolean isEvaluationDecided = application.isResultDecided();

        // 평가 상태 결정
        Stage evaluationStage;
        if (isAllEvaluatorsCompleted && isEvaluationDecided) {
            evaluationStage = Stage.AFTER;
        } else if (isAllEvaluatorsCompleted) {
            evaluationStage = DocumentEvaluationResponse.Stage.AFTER;
        } else if (currentEvaluator != null && currentEvaluator.getState().equals("ING")) {
            evaluationStage = DocumentEvaluationResponse.Stage.ING;
        } else {
            evaluationStage = DocumentEvaluationResponse.Stage.BEFORE;
        }

        // 응답 생성
        return new DocumentEvaluationResponse(
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

}

