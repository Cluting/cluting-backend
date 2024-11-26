package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class ClubUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clubId", nullable = false)
    private Club club;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Role role; // 부원 혹은 운영진

    @Column(nullable = true)
    private Integer permissionLevel;

    @OneToMany(mappedBy = "clubUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeSlot> timeSlots; // ClubUser가 가진 TimeSlot 리스트

    public enum Role {
        MEMBER, STAFF
    }
}

