package com.cluting.clutingbackend.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveAnswerRequestDto {
    private List<Answer> answers;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Answer {
        private Long questionId;
        private String content;
    }
}
