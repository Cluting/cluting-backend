package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
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

    @Column(length = 50, nullable = true)
    private String name;

    @Column(nullable = true)
    private Integer score;
    // Getters and Setters
}

