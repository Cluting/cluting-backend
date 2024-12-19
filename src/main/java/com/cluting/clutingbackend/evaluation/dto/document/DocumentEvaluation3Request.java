package com.cluting.clutingbackend.evaluation.dto.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentEvaluation3Request {
    private List<CriteriaEvaluation> criteriaEvaluations;
    private String comment;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CriteriaEvaluation {
        private Long criteriaId;
        private int score;
    }

}
