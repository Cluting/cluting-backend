package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evaluationId;

    @ManyToOne
    @JoinColumn(name = "criteriaId", nullable = false)
    private Criteria criteria;

    @ManyToOne
    @JoinColumn(name = "applicationId", nullable = false)
    private Application application;

    @ManyToOne
    @JoinColumn(name = "clubUserId", nullable = false)
    private ClubUser clubUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Stage stage;

    @Column(nullable = true)
    private Integer score;

    @Column(length = 500, nullable = true)
    private String comment;

    public enum Stage {
        DOCUMENT, INTERVIEW
    }

    // Getters and Setters
}

