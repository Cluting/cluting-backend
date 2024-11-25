package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private State state;

    @Column(nullable = true)
    private Integer numDone;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(length = 100, nullable = true)
    private String part;


    public enum State {
        SUBMITTED, REVIEWED, APPROVED
    }

    // Getters and Setters
}

