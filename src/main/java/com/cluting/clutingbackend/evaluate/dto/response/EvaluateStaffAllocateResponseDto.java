package com.cluting.clutingbackend.evaluate.dto.response;

import com.cluting.clutingbackend.evaluate.domain.Evaluator;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class EvaluateStaffAllocateResponseDto {
    private Long id;
    private List<String> staffs;
    private String partName;

    public static EvaluateStaffAllocateResponseDto toDto(Evaluator entity) {
        return EvaluateStaffAllocateResponseDto.builder()
                .id(entity.getEvaluatorId())
                .staffs(entity.getEvaluators().stream()
                        .map(evaluator -> evaluator.getUser().getName())
                        .toList())
                .partName(entity.getPartName())
                .build();
    }
}
