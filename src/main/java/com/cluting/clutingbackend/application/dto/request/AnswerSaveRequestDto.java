package com.cluting.clutingbackend.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerSaveRequestDto {
    private Long questionId;
    private String content;
}
