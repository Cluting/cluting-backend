package com.cluting.clutingbackend.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignInResponseDto {
    private String accessToken;
    private String refreshToken;
}
