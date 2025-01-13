package com.cluting.clutingbackend.evaluation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageResponseDto {
    private String pass;
    private String fail;
}
