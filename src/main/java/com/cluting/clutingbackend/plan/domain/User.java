package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(length = 100, nullable = true)
    private String email;

    @Column(length = 100, nullable = true)
    private String password;

    @Column(length = 100, nullable = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Role role;

    @Column(length = 15, nullable = true)
    private String phone;

    @Column(length = 100, nullable = true)
    private String location;

    @Column(length = 100, nullable = true)
    private String school;

    @Column(nullable = true)
    private Boolean isOnLeaver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Semester semester;

    @Column(length = 100, nullable = true)
    private String major;

    @Column(length = 100, nullable = true)
    private String doubleMajor;

    @Column(length = 255, nullable = true)
    private String profile;

    public enum Role {
        ADMIN, USER, GUEST
    }

    public enum Semester {
        S1_1, S1_2, S2_1, S2_2, S3_1, S3_2, S4_1, S4_2
    }

    // Getters and Setters
}

