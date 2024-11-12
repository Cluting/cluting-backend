package com.cluting.clutingbackend.plan.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PostRequestDto {
    private String title;
    private LocalDateTime recruitmentStartDate;
    private LocalDateTime recruitmentEndDate;
    private LocalDateTime documentResultDate;
    private LocalDateTime finalResultDate;
    private Integer recruitmentNumber;
    private LocalDate activityStart;
    private LocalDate activityEnd;
    private String activitySchedule;
    private Integer clubFee;
    private String content;
}

