package com.cluting.clutingbackend.docEval.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriteriaScoreDto {
    private String name; // 기준 이름
    private Integer score; // 기준에 대한 점수
}