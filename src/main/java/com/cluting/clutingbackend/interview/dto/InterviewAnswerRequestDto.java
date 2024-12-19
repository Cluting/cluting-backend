package com.cluting.clutingbackend.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewAnswerRequestDto {
    private Long interviewId; // 면접 ID
    private List<InterviewAnswerDto> answers; // 답변 리스트
}