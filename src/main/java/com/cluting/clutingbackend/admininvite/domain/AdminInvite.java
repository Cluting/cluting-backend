package com.cluting.clutingbackend.admininvite.domain;

import com.cluting.clutingbackend.club.domain.Club;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tb_admininvite")
public class AdminInvite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String uniqueInviteToken; // 토큰

    @Column
    private LocalDateTime expirationDate; // 만료일

    @Column
    private Boolean isUsed; // 사용유무

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;
}
