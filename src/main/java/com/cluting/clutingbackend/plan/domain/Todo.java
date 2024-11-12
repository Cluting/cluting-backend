package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;

@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(length = 255, nullable = true)
    private String content;

    @Column(nullable = true)
    private Boolean status;  // TRUE for completed, FALSE for not completed

}

