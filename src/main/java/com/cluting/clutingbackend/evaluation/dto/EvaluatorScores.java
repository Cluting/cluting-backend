package com.cluting.clutingbackend.evaluation.dto;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.plan.domain.DocumentEvalScore;
import com.cluting.clutingbackend.plan.domain.DocumentEvaluator;
import com.cluting.clutingbackend.plan.repository.DocumentEvalScoreRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
public class EvaluatorScores {
    private String evaluatorName;
    private List<CriteriaScore> scores;
    private Integer totalScore;

    public static EvaluatorScores of(DocumentEvaluator evaluator, DocumentEvalScoreRepository evalScoreRepository) {
        // DocumentEvaluator에 연결된 평가 점수 가져오기
        List<DocumentEvalScore> scores = evalScoreRepository.findByDocumentEvaluatorId(evaluator.getId());

        // CriteriaScore로 변환
        List<CriteriaScore> criteriaScores = scores.stream()
                .map(score -> CriteriaScore.of(
                        score.getDocumentCriteria().getName(),
                        score.getScore(),
                        score.getDocumentCriteria().getScore()
                ))
                .toList();

        // 총점 계산
        int totalScore = scores.stream()
                .mapToInt(DocumentEvalScore::getScore)
                .sum();

        return EvaluatorScores.of(
                evaluator.getClubUser().getUser().getName(),
                criteriaScores,
                totalScore
        );
    }

    public static EvaluatorScores ofForUser(DocumentEvaluator evaluator, ClubUser user, DocumentEvalScoreRepository evalScoreRepository) {
        // 특정 사용자가 평가한 점수 가져오기
        List<DocumentEvalScore> scores = evalScoreRepository.findByEvaluatorIdAndClubUserId(evaluator.getId(), user.getId());

        // CriteriaScore로 변환
        List<CriteriaScore> criteriaScores = scores.stream()
                .map(score -> CriteriaScore.of(
                        score.getDocumentCriteria().getName(),
                        score.getScore(),
                        score.getDocumentCriteria().getScore()
                ))
                .toList();

        // 총점 계산
        int totalScore = scores.stream()
                .mapToInt(DocumentEvalScore::getScore)
                .sum();

        return EvaluatorScores.of(
                user.getUser().getName(),
                criteriaScores,
                totalScore
        );
    }
}
