package com.cluting.clutingbackend.prep.dto;

import com.cluting.clutingbackend.recruit.dto.RecruitScheduleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class PrepDetailsResponseDto {
    private RecruitScheduleDto schedule;
    private List<PrepStageResponseDto> prepStages;
    private List<String> groups;
    private List<AdminInfoDto> admins;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AdminInfoDto {
        private Long id;       // 운영진 ID
        private String name;   // 운영진 이름
    }
}