package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;

@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    @ManyToOne
    @JoinColumn(name = "applicationId", nullable = false)
    private Application application;

    @Column(nullable = true)
    private String content;

    @Column(nullable = true)
    private Integer questionId;

    // Getters and Setters
}

