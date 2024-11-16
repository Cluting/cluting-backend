package com.cluting.clutingbackend.prep.dto;

import com.cluting.clutingbackend.plan.domain.RecruitSchedule;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RecruitScheduleDto {
    private Date stage1Start;
    private Date stage1End;
    private Date stage2Start;
    private Date stage2End;
    private Date stage3Start;
    private Date stage3End;
    private Date stage4Start;
    private Date stage4End;
    private Date stage5Start;
    private Date stage5End;
    private Date stage6Start;
    private Date stage6End;
    private Date stage7Start;
    private Date stage7End;
    private Date stage8Start;
    private Date stage8End;

    @JsonCreator
    public RecruitScheduleDto(RecruitSchedule recruitSchedule) {
        this.stage1Start = recruitSchedule.getStage1Start();
        this.stage1End = recruitSchedule.getStage1End();
        this.stage2Start = recruitSchedule.getStage2Start();
        this.stage2End = recruitSchedule.getStage2End();
        this.stage3Start = recruitSchedule.getStage3Start();
        this.stage3End = recruitSchedule.getStage3End();
        this.stage4Start = recruitSchedule.getStage4Start();
        this.stage4End = recruitSchedule.getStage4End();
        this.stage5Start = recruitSchedule.getStage5Start();
        this.stage5End = recruitSchedule.getStage5End();
        this.stage6Start = recruitSchedule.getStage6Start();
        this.stage6End = recruitSchedule.getStage6End();
        this.stage7Start = recruitSchedule.getStage7Start();
        this.stage7End = recruitSchedule.getStage7End();
        this.stage8Start = recruitSchedule.getStage8Start();
        this.stage8End = recruitSchedule.getStage8End();
    }
}
