package com.cluting.clutingbackend.application.dto.response;

import lombok.Data;

@Data
public class DocumentAnswerResponseDto {

    private String questionContent;
    private String answerContent;

    public DocumentAnswerResponseDto(String questionContent, String answerContent) {
        this.questionContent = questionContent;
        this.answerContent = answerContent;
    }

}
