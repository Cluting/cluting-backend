package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interviewId;

    @ManyToOne
    @JoinColumn(name = "applicationId", nullable = false)
    private Application application;

    @Column(nullable = true)
    private Date date;

    @Column(length = 255, nullable = true)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private State state;

    public enum State {
        BEFORE, INPROCESS, AFTER, COMPLETED
    }
}

