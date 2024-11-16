package com.cluting.clutingbackend.plan.dto.request;

import com.cluting.clutingbackend.plan.domain.User;
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
    private User.Semester semester;
    private String major;
    private String doubleMajor;

    public User toEntity(String encoded) {
        return User.builder()
                .name(name)
                .role(User.Role.USER)
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
