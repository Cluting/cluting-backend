package com.cluting.clutingbackend.clubuser.domain;

import com.cluting.clutingbackend.club.domain.Club;
import com.cluting.clutingbackend.global.enums.PermissionLevel;
import com.cluting.clutingbackend.global.security.PermissionLevelConverter;
import com.cluting.clutingbackend.interview.domain.InterviewTimeSlot;
import com.cluting.clutingbackend.todo.domain.Todo;
import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.global.enums.ClubRole;
import jakarta.persistence.*;
import lombok.*;

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

    @Convert(converter = PermissionLevelConverter.class)
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "TEXT",name = "permission_levels")
    private List<PermissionLevel> permissionLevels; // 모집하기 단계에서의 권한 체크를 위한 enum

    @Column(nullable = true)
    private Integer generation;

    @OneToMany(mappedBy = "clubUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterviewTimeSlot> timeSlots; // ClubUser가 가진 TimeSlot 리스트
}
