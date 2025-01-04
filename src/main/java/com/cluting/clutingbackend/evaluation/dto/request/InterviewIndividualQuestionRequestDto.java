package com.cluting.clutingbackend.evaluation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewIndividualQuestionRequestDto {
    private List<String> question;
}
