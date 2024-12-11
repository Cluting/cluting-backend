package com.cluting.clutingbackend.evaluation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentEvaluationCompleteRequest {
    private String name;        // 사용자의 이름
    private String phone;       // 사용자의 전화번호
    private String state;       // 상태 (PASS 또는 FAIL)
}
