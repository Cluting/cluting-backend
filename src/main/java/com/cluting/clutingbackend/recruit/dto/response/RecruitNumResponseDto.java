package com.cluting.clutingbackend.recruit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class RecruitNumResponseDto {
    private Integer num;
    private Map<String, Integer> map;
}
