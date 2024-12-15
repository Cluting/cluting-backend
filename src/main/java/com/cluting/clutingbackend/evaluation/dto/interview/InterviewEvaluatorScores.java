package com.cluting.clutingbackend.evaluation.dto.interview;

import com.cluting.clutingbackend.evaluation.dto.document.EvaluatorScores;
import com.cluting.clutingbackend.interview.domain.InterviewEvaluator;
import com.cluting.clutingbackend.interview.repository.InterviewScoreRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(staticName = "of")
public class InterviewEvaluatorScores {
    private final String evaluatorName;
    private final List<EvaluationDetail> scores;

    public static InterviewEvaluatorScores of(InterviewEvaluator evaluator, InterviewScoreRepository interviewScoreRepository) {
        List<EvaluationDetail> scores = interviewScoreRepository.findByInterviewEvaluatorId(evaluator.getId()).stream()
                .map(EvaluationDetail::of)
                .collect(Collectors.toList());

        return new InterviewEvaluatorScores(
                evaluator.getClubUser().getUser().getName(),
                scores
        );
    }
}
