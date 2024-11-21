package com.cluting.clutingbackend.docEval.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminEvaluation {
    private String name;
    private Integer totalScore;
    private List<CriteriaScore> criteriaScores;
    private String comment;
}