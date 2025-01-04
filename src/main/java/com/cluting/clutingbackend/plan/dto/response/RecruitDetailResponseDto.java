package com.cluting.clutingbackend.plan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruitDetailResponseDto {
    private Long recruitId;
    private String title;
    private Integer numDoc;
    private Integer numFinal;
    private List<IdealResponse> ideals;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdealResponse {
        private Long id;
        private String content;
    }
}
