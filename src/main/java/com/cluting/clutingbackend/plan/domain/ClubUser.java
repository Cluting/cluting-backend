package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ClubUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubUserId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "clubId", nullable = false)
    private Club club;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Role role;

    @Column(nullable = true)
    private Integer permissionLevel;

    public enum Role {
        MEMBER, STAFF
    }

    // Getters and Setters
}

