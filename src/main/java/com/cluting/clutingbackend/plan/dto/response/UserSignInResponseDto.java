package com.cluting.clutingbackend.plan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignInResponseDto {
    private String accessToken;
    private String refreshToken;
}
