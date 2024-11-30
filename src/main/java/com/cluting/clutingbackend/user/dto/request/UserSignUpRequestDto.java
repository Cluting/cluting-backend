package com.cluting.clutingbackend.user.dto.request;

import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.global.enums.Role;
import com.cluting.clutingbackend.global.enums.Semester;
import com.cluting.clutingbackend.global.enums.StudentStatus;
import lombok.Getter;

@Getter
public class UserSignUpRequestDto {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String location;
    private String school;
    private StudentStatus studentStatus;
    private Semester semester;
    private String major;
    private String doubleMajor;
    private Boolean termsOfService;
    private Boolean privacyPolicy;
    private Boolean marketingConsent;

    public User toEntity(String encoded) {
        return User.builder()
                .name(name)
                .role(Role.GENERAL)
                .email(email)
                .password(encoded)
                .phone(phone)
                .location(location)
                .school(school)
                .studentStatus(studentStatus)
                .semester(semester)
                .major(major)
                .doubleMajor(doubleMajor)
                .termsOfService(termsOfService)
                .privacyPolicy(privacyPolicy)
                .marketingConsent(marketingConsent)
                .build();
    }
}
