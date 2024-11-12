package com.cluting.clutingbackend.user.dto;

import com.cluting.clutingbackend.user.domain.enums.Role;
import com.cluting.clutingbackend.user.domain.enums.Semester;
import com.cluting.clutingbackend.user.domain.User;
import lombok.Getter;

@Getter
public class UserSignUpRequestDto {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String location;
    private String school;
    private Boolean isOnLeaver;
    private Semester semester;
    private String major;
    private String doubleMajor;

    public User toEntity(String encoded) {
        return User.builder()
                .name(name)
                .role(Role.GENERAL)
                .email(email)
                .password(encoded)
                .phone(phone)
                .location(location)
                .school(school)
                .isOnLeaver(isOnLeaver)
                .semester(semester)
                .major(major)
                .doubleMajor(doubleMajor)
                .build();
    }
}
