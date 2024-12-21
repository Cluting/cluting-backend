package com.cluting.clutingbackend.interview.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterviewScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_criteria_id", nullable = false)
    private InterviewCriteria interviewCriteria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_evaluator_id", nullable = false)
    private InterviewEvaluator interviewEvaluator;

    @Column(nullable = false)
    private Integer score;
}
