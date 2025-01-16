package com.cluting.clutingbackend.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class GroupQuestionResponseDto {
    private Map<String, List<DocumentQuestionResponseDto>> questions;
}
