package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;

@Entity
public class InterviewAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String content;

}
