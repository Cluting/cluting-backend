package com.cluting.clutingbackend.prep.dto;

import com.cluting.clutingbackend.recruit.dto.RecruitScheduleDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PrepDetailsDto {
    private RecruitScheduleDto schedule;
    private List<PrepStageDto> prepStages;
    private List<String> groups;
}