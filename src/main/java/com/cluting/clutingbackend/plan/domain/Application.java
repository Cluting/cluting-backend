package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private State state;

    @Column(nullable = true)
    private Integer score;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    @Column(length = 100, nullable = true)
    private String part;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    public enum State {
        BEFORE, INPROCESS, DOCPASS, DOCFAIL, AFTER, COMPLETED, OBJECTION, RESOLVED, PASS, FAIL
    }

    // Getters and Setters
}
