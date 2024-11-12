package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubId;

    @Column(length = 100, nullable = true)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(length = 255, nullable = true)
    private String profile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Type type;

    @Column(length = 100, nullable = true)
    private String keyword;

    @Column(nullable = true)
    private Boolean isRecruiting;

    public enum Category {
        CULTURE, PHYSICAL, STARTUP, LANGUAGE, SOCIAL, TECHNOLOGY, SERVICE, ACADEMIC, ELSE
    }

    public enum Type {
        INTERNAL, EXTERNAL
    }

    // Getters and Setters
}
