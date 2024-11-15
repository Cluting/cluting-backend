package com.cluting.clutingbackend.prep.dto;

import com.cluting.clutingbackend.plan.domain.RecruitSchedule;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PrepRequestDto {
    private RecruitScheduleDto recruitSchedules;
    private List<PrepStageDto> prepStages;
    private List<RecruitGroupDto> recruitGroups;
}
