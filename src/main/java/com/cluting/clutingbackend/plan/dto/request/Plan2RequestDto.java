package com.cluting.clutingbackend.plan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plan2RequestDto{

    private List<PartProfile> partProfiles; //공통 인재상 + 파트별 인재상

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PartProfile {
        private String partName;         // 파트 이름 ( 공통 인재상이면, "공통"으로 명명하기)
        private List<String> profiles;  // 해당 파트의 인재상 목록
    }
}
