package com.cluting.clutingbackend.recruit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RecruitsResponseDto {
    private Integer count;
    private List<RecruitResponseDto> recruits;
}
