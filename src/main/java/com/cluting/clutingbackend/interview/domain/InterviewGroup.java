package com.cluting.clutingbackend.interview.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "interview_evaluator_id", nullable = false)
    private InterviewEvaluator interviewEvaluator;

    @Column(nullable = true)
    private Boolean isAssigned; //최종 할당

    @Column(nullable = true)
    private LocalDateTime time;
}
