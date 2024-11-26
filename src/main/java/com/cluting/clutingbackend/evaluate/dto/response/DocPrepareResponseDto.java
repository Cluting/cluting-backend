package com.cluting.clutingbackend.evaluate.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DocPrepareResponseDto {
    List<EvaluateStaffAllocateResponseDto> evaluators;
    List<CriteriaCreateResponseDto> criteria;

    public static DocPrepareResponseDto toDto(List<EvaluateStaffAllocateResponseDto> evaluators, List<CriteriaCreateResponseDto> criteria) {
        return DocPrepareResponseDto.builder()
                .evaluators(evaluators)
                .criteria(criteria)
                .build();
    }
}
