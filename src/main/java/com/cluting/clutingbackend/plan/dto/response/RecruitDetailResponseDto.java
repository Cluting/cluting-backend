package com.cluting.clutingbackend.plan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecruitDetailResponseDto {
    private Long recruitId;
    private String title;
    private Integer numDoc;
    private Integer numFinal;
    private List<GroupResponse> groupResponses;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GroupResponse {
        private Long groupId;
        private Map<Long,String> idealContent;
        private Integer numDoc;
        private Integer numFinal;
    }
}
