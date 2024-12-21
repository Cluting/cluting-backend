package com.cluting.clutingbackend.evaluation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class DocumentEvaluateResultsResponseDto {
    // 합격자
    private Integer passedCnt;
    private Map<String, Integer> byGroup;
    private List<DocumentEvaluateResultResponseDto> passed;

    // 불합격자
    private Integer failedCnt;
    private List<DocumentEvaluateResultResponseDto> failed;
}
