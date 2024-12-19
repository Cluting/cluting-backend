package com.cluting.clutingbackend.interview.dto;

import com.cluting.clutingbackend.global.enums.QuestionType2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewAnswerDto {
    private Long questionId; // 질문 ID
    private QuestionType2 type; // 질문 타입 (COMMON, PART_SPECIFIC, PERSONAL)
    private String content; // 답변 내용
}
