package com.cluting.clutingbackend.plan.dto.response;

import com.cluting.clutingbackend.global.enums.SecondStage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SecondStageResponseDto {
    private String stageName; // 스테이지 이름
    private String description; // 설명
    private SecondStage.CompleteState completeState; // 완료 여부
}
