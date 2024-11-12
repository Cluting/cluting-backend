package com.cluting.clutingbackend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignInResponseDto {
    private String accessToken;
    private String refreshToken;
}
