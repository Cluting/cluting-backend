package com.cluting.clutingbackend.interview.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
