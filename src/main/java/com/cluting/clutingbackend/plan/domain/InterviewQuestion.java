package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;

@Entity
public class InterviewQuestion {
    @Id
    private String questionKey;

    @ManyToOne
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;

    @Column(length = 255, nullable = true)
    private String content;

    // Getters and Setters
}

