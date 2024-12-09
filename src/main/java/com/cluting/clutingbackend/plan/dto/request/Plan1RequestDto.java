package com.cluting.clutingbackend.plan.dto.request;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plan1RequestDto {

    // 전체 서류 합격 인원
    private Integer totalDocumentPassCount;

    // 전체 최종 합격 인원
    private Integer totalFinalPassCount;

    // 파트 정보
    private List<GroupInfo> groupInfos;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupInfo {
        private String groupName;              // 파트 이름
        private Integer documentPassCount;   // 서류 합격 인원
        private Integer finalPassCount;      // 최종 합격 인원
    }
}

