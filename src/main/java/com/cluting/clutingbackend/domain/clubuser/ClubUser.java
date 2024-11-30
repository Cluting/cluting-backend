package com.cluting.clutingbackend.domain.clubuser;

import com.cluting.clutingbackend.domain.club.Club;
import com.cluting.clutingbackend.domain.user.User;
import com.cluting.clutingbackend.global.enums.ClubRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tb_clubuser")
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

    @Column(nullable = true)
    private Integer permissionLevel;

    @Column(nullable = true)
    private Integer generation;

//    @OneToMany(mappedBy = "clubUser", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<TimeSlot> timeSlots; // ClubUser가 가진 TimeSlot 리스트
}
