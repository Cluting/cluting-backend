package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;

@Entity
public class Criteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long criteriaId;

    @ManyToOne
    @JoinColumn(name = "partId", nullable = false)
    private Part part;

    @ManyToOne
    @JoinColumn(name = "interviewId", nullable = true)
    private Interview interview;

    @Column(length = 255, nullable = true)
    private String content;

    // Getters and Setters
}

