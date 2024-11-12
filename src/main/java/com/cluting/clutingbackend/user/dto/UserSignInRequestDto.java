package com.cluting.clutingbackend.user.dto;

import lombok.Getter;

@Getter
public class UserSignInRequestDto {
    private String email;
    private String password;
}
