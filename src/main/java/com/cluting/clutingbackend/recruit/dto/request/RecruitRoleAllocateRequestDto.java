package com.cluting.clutingbackend.recruit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecruitRoleAllocateRequestDto {
    private String groupName;
    private List<Long> admins;
    private List<RecruitCriteriaSaveRequestDto> criteria;
    private Integer maxScore;
}
