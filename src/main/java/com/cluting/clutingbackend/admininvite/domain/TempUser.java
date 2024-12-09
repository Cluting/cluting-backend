package com.cluting.clutingbackend.admininvite.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
// [운영진 초대] 비회원 유저 운영진 초대 시, 비회원 정보를 저장하기 위한 엔티티
public class TempUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @OneToOne
    private AdminInvite invite;

    @Builder
    public TempUser(String email, AdminInvite invite) {
        this.email = email;
        this.invite = invite;
    }
}
