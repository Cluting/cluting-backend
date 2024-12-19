package com.cluting.clutingbackend.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InterviewDetailsDto {
    private LocalDateTime interviewTime;
    private String dayOfWeek;
    private IntervieweeInfoDto intervieweeInfo;
    private List<QuestionAnswerDto> commonQuestions;
    private List<QuestionAnswerDto> groupQuestions;
    private Map<Long, List<QuestionAnswerDto>> personalQuestions;
}