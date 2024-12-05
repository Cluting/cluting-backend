package com.cluting.clutingbackend.prep.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PrepStageResponseDto {
    private String stageName;
    private List<String> adminNames;
}