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
                                            evaluator.getStage() == Stage.ING);

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
                .map(application -> mapToResponse(application, recruitId, currentUser))
                .collect(Collectors.toList());
    }


    private DocumentEvaluationResponse mapToResponse(
            Application application,
            Long recruitId,
            CustomUserDetails currentUser
    ) {
        User user = application.getUser();
        List<DocumentEvaluator> evaluators = documentEvaluatorRepository.findByApplicationId(application.getId());

        // 현재 로그인한 유저의 상태 확인
        DocumentEvaluationResponse.EvaluatorInfo currentEvaluator = evaluators.stream()
                .filter(evaluator -> evaluator.getClubUser().getUser().getId().equals(currentUser.getId()))
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

        // 모든 운영진이 평가 완료 상태인지 확인
        boolean isAllEvaluatorsAfter = evaluators.stream()
                .allMatch(evaluator -> evaluator.getStage() == Stage.AFTER);

        // 평가 결과 결정 여부 확인
        boolean isFinalDecisionMade = application.isCompleted();

        // 평가 상태 결정
        Stage evaluationStage;

        if (isFinalDecisionMade) {
            // 평가 완료
            evaluationStage = Stage.AFTER; // 이 시점에서 이미 모든 운영진이 평가를 완료한 상태임
        } else if (isAllEvaluatorsAfter) {
            // 모든 운영진이 평가 완료 상태 (하지만 최종 판단은 안 됨)
            evaluationStage = Stage.EDITABLE;
        } else if (currentEvaluator != null && currentEvaluator.getState().equals("ING")) {
            // 현재 유저가 평가 중
            evaluationStage = Stage.ING;
        } else {
            // 평가 전
            evaluationStage = Stage.BEFORE;
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

