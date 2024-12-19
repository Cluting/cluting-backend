package com.cluting.clutingbackend.evaluation.dto.interview;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewEvaluationRequestDto {
    private List<CriteriaEvaluation> criteriaEvaluations;
    private String comment;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CriteriaEvaluation {
        private Long criteriaId;
        private Integer score;
    }
}
