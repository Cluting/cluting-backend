package com.cluting.clutingbackend.prep.dto;

import com.cluting.clutingbackend.plan.domain.ClubUser;
import com.cluting.clutingbackend.prep.domain.PrepStage;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PrepStageDto {

    private String stageName;
    private int stageOrder;
    private List<Long> clubUserIds;

    @JsonCreator
    public PrepStageDto(
            @JsonProperty("stageName") String stageName,
            @JsonProperty("stageOrder") int stageOrder,
            @JsonProperty("clubUserIds") List<Long> clubUserIds) {
        this.stageName = stageName;
        this.stageOrder = stageOrder;
        this.clubUserIds = clubUserIds;
    }

    // 기존 생성자
    public PrepStageDto(PrepStage prepStage) {
        this.stageName = prepStage.getStageName();
        this.stageOrder = prepStage.getStageOrder();
        this.clubUserIds = prepStage.getPrepStageClubUsers().stream()
                .map(clubUser -> clubUser.getClubUser().getClubUserId())
                .collect(Collectors.toList());
    }
}
