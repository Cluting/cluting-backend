package com.cluting.clutingbackend.docEval.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor // 모든 필드에 대한 생성자 자동 생성
public class ScoreDetail {
    private Long criteriaId;
    private Integer score;
}