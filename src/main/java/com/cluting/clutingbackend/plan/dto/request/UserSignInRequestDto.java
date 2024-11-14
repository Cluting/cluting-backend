package com.cluting.clutingbackend.plan.dto.request;

import lombok.Getter;

@Getter
public class UserSignInRequestDto {
    private String email;
    private String password;
}
