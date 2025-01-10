package com.cluting.clutingbackend.clubuser.domain;

import com.cluting.clutingbackend.club.domain.Club;
import com.cluting.clutingbackend.global.enums.PermissionLevel;
import com.cluting.clutingbackend.interview.domain.InterviewTimeSlot;
import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.global.enums.ClubRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tb_club_user")
public class ClubUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private ClubRole role; // 부원 혹은 운영진

    @OneToMany(mappedBy = "clubUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<ClubUserPermission> permissionLevels = new ArrayList<>(); // ClubUser의 권한 리스트// 모집하기 단계에서의 권한 체크를 위한 enum

    @Column(nullable = true)
    private Integer generation;

    @OneToMany(mappedBy = "clubUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterviewTimeSlot> timeSlots; // ClubUser가 가진 TimeSlot 리스트

    @Column(nullable = true)
    @Builder.Default
    private String interviewGroup = "";


    public static ClubUser of(
            User user,
            Club club,
            ClubRole role,
            Integer generation
//            List<ClubUserPermission> permissionLevels
    ) {
        return ClubUser.builder()
                .user(user)
                .club(club)
                .role(role)
//                .permissionLevels(permissionLevels)
                .generation(generation)
                .build();
    }
    @Override
    public String toString() {
        return String.format("[ClubUser 객체] id : %s, role : %s, club : %s, permissionLevels : %s", this.id, this.role, this.club, this.permissionLevels.toString());
    }

    public void setInterviewGroup(String interviewGroup) {
        this.interviewGroup = interviewGroup;
    }
}
