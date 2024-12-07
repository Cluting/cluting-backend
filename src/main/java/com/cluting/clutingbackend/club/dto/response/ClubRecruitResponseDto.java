package com.cluting.clutingbackend.club.dto.response;

import com.cluting.clutingbackend.global.enums.CurrentStage;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ClubRecruitResponseDto {
    private Long id;
    private String title;
    private String description;
    private String image;
    private Boolean isDone;
    private String caution;
    private LocalDateTime deadLine;
    private CurrentStage currentStage;
    private Boolean isInterview;
    private String applicationTitle;
    private Boolean isRequiredPortfolio;
    private Integer interviewerCount;
    private Integer intervieweeCount;
    private Integer interviewDuration;

    public static ClubRecruitResponseDto toDto(Recruit entity) {
        return ClubRecruitResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .image(entity.getImage())
                .isDone(entity.getIsDone())
                .caution(entity.getCaution())
                .deadLine(entity.getDeadLine())
                .currentStage(entity.getCurrentStage())
                .isInterview(entity.getIsInterview())
                .applicationTitle(entity.getApplicationTitle())
                .isRequiredPortfolio(entity.getIsRequiredPortfolio())
                .interviewerCount(entity.getInterviewerCount())
                .intervieweeCount(entity.getIntervieweeCount())
                .interviewDuration(entity.getInterviewDuration())
                .build();
    }
}
