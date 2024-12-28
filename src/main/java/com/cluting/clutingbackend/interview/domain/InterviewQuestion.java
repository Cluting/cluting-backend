package com.cluting.clutingbackend.interview.domain;

import com.cluting.clutingbackend.global.enums.QuestionType2;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tb_interview_question")
public class InterviewQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_evaluator_id", nullable = true)
    private InterviewEvaluator interviewEvaluator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_answer_id", nullable = true)
    private InterviewAnswer interviewAnswer;

    @Lob
    @Column(nullable = true)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private QuestionType2 type;

    public static InterviewQuestion of(
            InterviewEvaluator interviewEvaluator,
            Interview interview,
            String content,
            QuestionType2 type
    ) {
        return InterviewQuestion.builder()
                .interviewEvaluator(interviewEvaluator)
                .interview(interview)
                .interviewAnswer(null)
                .content(content)
                .type(type)
                .build();
    }
}
