package com.cluting.clutingbackend.interview.domain;


import com.cluting.clutingbackend.global.enums.QuestionType;
import com.cluting.clutingbackend.global.enums.QuestionType2;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "interview_evaluator_id", nullable = false)
    private InterviewEvaluator interviewEvaluator;

    @ManyToOne
    @JoinColumn(name = "interviewI_id", nullable = false)
    private Interview interview;

    @ManyToOne
    @JoinColumn(name = "interview_answer_id", nullable = true)
    private InterviewAnswer interviewAnswer;

    private String content;

    @Enumerated(EnumType.STRING)
    private QuestionType2 type;
}
