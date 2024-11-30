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

    @ManyToOne
    @JoinColumn(name = "interview_evaluator_id", nullable = false)
    private InterviewEvaluator interviewEvaluator;

    private String name;
    private String content;
    private Integer score;
}
