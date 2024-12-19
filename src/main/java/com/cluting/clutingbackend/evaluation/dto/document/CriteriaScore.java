package com.cluting.clutingbackend.evaluation.dto.document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class CriteriaScore {
    private String criteriaContent;
    private Integer score;
    private Integer maxScore;
}