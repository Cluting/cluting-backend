package com.cluting.clutingbackend.evaluation.dto;

import com.cluting.clutingbackend.plan.domain.Option;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class OptionResponse {
    private String content;
    private Boolean isCorrect;

    public static OptionResponse of(Option option) {
        return OptionResponse.of(option.getContent(), option.isCorrect());
    }
}