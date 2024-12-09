package com.cluting.clutingbackend.prep.dto;

import lombok.*;

import java.util.List;

// [계획하기] 모집 단계별 운영진 목록
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepStageDto {
    private String stageName;
    private Integer stageOrder;
    private List<Long> clubUserIds; // 운영진 ID 목록
}
