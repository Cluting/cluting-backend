package com.cluting.clutingbackend.interview.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tb_interview_criteria")
public class InterviewCriteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_evaluator_id", nullable = false)
    private InterviewEvaluator interviewEvaluator;

    @Column(length = 100, nullable = true)
    private String name;

    @Lob
    @Column(nullable = true)
    private String content;

    @Column(nullable = true)
    private Integer score;

    public static InterviewCriteria of(
            InterviewEvaluator interviewEvaluator,
            String name,
            String content,
            Integer score
    ) {
        return InterviewCriteria.builder()
                .interviewEvaluator(interviewEvaluator)
                .name(name)
                .content(content)
                .score(score)
                .build();
    }
}
