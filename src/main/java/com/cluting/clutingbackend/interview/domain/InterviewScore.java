package com.cluting.clutingbackend.interview.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class InterviewScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "interview_criteria_id", nullable = false)
    private InterviewCriteria interviewCriteria;

    @ManyToOne
    @JoinColumn(name = "interview_evaluator_id", nullable = false)
    private InterviewEvaluator interviewEvaluator;

    private Integer score;
}
