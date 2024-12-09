package com.cluting.clutingbackend.recruit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecruitDocSetRequestDto {
    private List<RecruitRoleAllocateRequestDto> groups;
}
