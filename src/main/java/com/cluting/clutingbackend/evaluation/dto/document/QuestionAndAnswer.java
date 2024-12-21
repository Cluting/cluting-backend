package com.cluting.clutingbackend.evaluation.dto.document;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
public class QuestionAndAnswer {
    private String question;
    private String answer;
    private List<OptionResponse> options;
}