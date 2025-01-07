package com.cluting.clutingbackend.evaluation.dto.document;

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
    private String comment;

    public static EvaluatorScores of(DocumentEvaluator evaluator, DocumentEvalScoreRepository evalScoreRepository) {
        List<DocumentEvalScore> scores = evalScoreRepository.findByDocumentEvaluatorId(evaluator.getId());
        List<CriteriaScore> criteriaScores = scores.stream()
                .map(score -> CriteriaScore.of(
                        score.getDocumentCriteria().getName(),
                        score.getScore(),
                        score.getDocumentCriteria().getScore()
                ))
                .toList();
        int totalScore = scores.stream().mapToInt(DocumentEvalScore::getScore).sum();

        // 코멘트 가져오기
        String comment = evaluator.getComment();

        return EvaluatorScores.of(
                evaluator.getClubUser().getUser().getName(),
                criteriaScores,
                totalScore,
                comment
        );
    }

    public static EvaluatorScores ofForUser(DocumentEvaluator evaluator, ClubUser user, DocumentEvalScoreRepository evalScoreRepository) {
        List<DocumentEvalScore> scores = evalScoreRepository.findByEvaluatorIdAndClubUserId(evaluator.getId(), user.getId());
        List<CriteriaScore> criteriaScores = scores.stream()
                .map(score -> CriteriaScore.of(
                        score.getDocumentCriteria().getName(),
                        score.getScore(),
                        score.getDocumentCriteria().getScore()
                ))
                .toList();
        int totalScore = scores.stream().mapToInt(DocumentEvalScore::getScore).sum();

        // 코멘트 가져오기
        String comment = evaluator.getComment();

        return EvaluatorScores.of(
                user.getUser().getName(),
                criteriaScores,
                totalScore,
                comment
        );
    }
}
