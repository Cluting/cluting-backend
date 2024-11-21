package com.cluting.clutingbackend.docEval.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // 모든 필드에 대한 생성자 자동 생성
public class QuestionContent {
    private Integer questionId;
    private String content;
}