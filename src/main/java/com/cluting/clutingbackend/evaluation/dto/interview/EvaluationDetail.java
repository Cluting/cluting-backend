package com.cluting.clutingbackend.evaluation.dto.interview;

import com.cluting.clutingbackend.interview.domain.InterviewScore;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class EvaluationDetail {
    private final String criterion;
    private final Integer score;

    public static EvaluationDetail of(InterviewScore score) {
        return new EvaluationDetail(score.getInterviewCriteria().getName(), score.getScore());
    }
}
