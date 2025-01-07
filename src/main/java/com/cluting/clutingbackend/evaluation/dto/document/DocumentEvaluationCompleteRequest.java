package com.cluting.clutingbackend.evaluation.dto.document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentEvaluationCompleteRequest {
    private  Long id;
    private String state;       // 상태 (PASS 또는 FAIL)
}
