package com.cluting.clutingbackend.recruit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CurrentStageResponseDto {
    private String stage; // CurrentStage 이름
    private String description; // 단계 설명
    private String state; // 상태 (BEFORE, IN_PROGRESS, AFTER)
}

