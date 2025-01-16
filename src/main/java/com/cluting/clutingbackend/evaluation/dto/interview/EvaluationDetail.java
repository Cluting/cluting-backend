package com.cluting.clutingbackend.evaluation.dto.interview;

import com.cluting.clutingbackend.interview.domain.InterviewCriteria;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class EvaluationDetail {
    private Long criteriaId;
    private final String criterion;
    private final Integer score;

    public static EvaluationDetail of(InterviewCriteria criteria) {
        return new EvaluationDetail(
                criteria.getId(),
                criteria.getName(),
                criteria.getScore()
        );
    }
}
