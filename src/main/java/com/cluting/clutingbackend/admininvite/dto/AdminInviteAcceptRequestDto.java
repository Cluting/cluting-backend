package com.cluting.clutingbackend.admininvite.dto;

import lombok.Getter;
import lombok.Setter;

// [운영진 초대] 초대 수락 시, 토큰과 이메일을 통해 운영진 수락됨.
@Getter
@Setter
public class AdminInviteAcceptRequestDto {
    private String token;
    private String email;
}