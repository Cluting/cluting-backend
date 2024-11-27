package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(nullable = true)
    private String content;


    @ManyToOne
    @JoinColumn(name="question_id")
    private Question question;

    // Getters and Setters
}

