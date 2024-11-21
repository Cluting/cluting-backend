package com.cluting.clutingbackend.docEval.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationRequest {

    private List<CriteriaScoreDto> criteriaScores; // 기준 이름별 점수
    private String comment; // 코멘트

}