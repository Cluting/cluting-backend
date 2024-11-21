package com.cluting.clutingbackend.docEval.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor // 모든 필드에 대한 생성자 자동 생성
public class MyEvaluation {
    private List<ScoreDetail> scoreDetails;
    private String comment;
}