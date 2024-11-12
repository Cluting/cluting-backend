package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "clubId", nullable = false)
    private Club club;

    @Column(length = 100, nullable = true)
    private String title;

    @Column(nullable = true)
    private String description;

    @Column(length = 255, nullable = true)
    private String profile;

    @Column(nullable = true)
    private Boolean isDone;

    @Column(length = 255, nullable = true)
    private String caution;

    // Getters and Setters
}

