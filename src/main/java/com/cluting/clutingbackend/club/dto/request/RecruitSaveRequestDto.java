package com.cluting.clutingbackend.club.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecruitSaveRequestDto {
    private Integer generation;
    private Boolean isInterview;
}
