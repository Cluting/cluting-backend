package com.cluting.clutingbackend.plan.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plan1ResponseDto {
    private Long recruitId;                // Recruit의 ID
    private String title;                  // 공고 제목
    private String description;            // 공고 설명
    private Integer totalDocumentPassCount; // 전체 서류 합격 인원
    private Integer totalFinalPassCount;    // 전체 최종 합격 인원
    private List<PartInfo> parts;          // 파트 정보 리스트

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PartInfo {
        private String partName;           // 파트 이름
        private Integer documentPassCount; // 서류 합격 인원
        private Integer finalPassCount;    // 최종 합격 인원
    }
}

