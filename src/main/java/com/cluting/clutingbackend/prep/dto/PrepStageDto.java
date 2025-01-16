package com.cluting.clutingbackend.prep.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PrepStageDto {
    private String stageName;
    private Integer stageOrder;
    private List<AdminInfoDto> admins;

    @Data
    public static class AdminInfoDto {
        public AdminInfoDto(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        private Long id;       // 운영진 ID
        private String name;   // 운영진 이름

        public AdminInfoDto() {}
    }
}