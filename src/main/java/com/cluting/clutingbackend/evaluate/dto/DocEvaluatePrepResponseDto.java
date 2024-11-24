package com.cluting.clutingbackend.evaluate.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
// 서류 평가 준비에 필요한 DTO
public class DocEvaluatePrepResponseDto {
    private Integer totalNum; // 전체 지원자 수 (모든 파트)
    private Integer totalDocNum; // 전체 서류 합격자 수
    private List<PartResponseDto> parts; // 파트별 지원자 수

    public static DocEvaluatePrepResponseDto toDto(Integer totalNum, Integer totalDocNum, List<PartResponseDto> parts) {
        return DocEvaluatePrepResponseDto.builder()
                .totalNum(totalNum)
                .totalDocNum(totalDocNum)
                .parts(parts)
                .build();
    }
}
