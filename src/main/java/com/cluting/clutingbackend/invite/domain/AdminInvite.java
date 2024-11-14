package com.cluting.clutingbackend.invite.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class AdminInvite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String uniqueInviteToken;

    @Column(nullable = false)
    private Long clubId;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private boolean isUsed = false;

    public AdminInvite(String uniqueInviteToken, Long clubId, LocalDateTime expirationDate) {
        this.uniqueInviteToken = uniqueInviteToken;
        this.clubId = clubId;
        this.expirationDate = expirationDate;
    }

    public void markAsUsed() {
        this.isUsed = true;
    }
}