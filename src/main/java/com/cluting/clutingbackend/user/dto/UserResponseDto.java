package com.cluting.clutingbackend.user.dto;

import com.cluting.clutingbackend.user.domain.enums.Role;
import com.cluting.clutingbackend.user.domain.enums.Semester;
import com.cluting.clutingbackend.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private Long id;
    private String email;
    private String name;
    private Role role;
    private String phone;
    private String location;
    private String school;
    private Boolean isOnLeaver;
    private Semester semester;
    private String major;
    private String doubleMajor;
//    private String profile;

    public static UserResponseDto toDto(User entity) {
        return UserResponseDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .name(entity.getName())
                .role(entity.getRole())
                .phone(entity.getPhone())
                .location(entity.getLocation())
                .school(entity.getSchool())
                .isOnLeaver(entity.getIsOnLeaver())
                .semester(entity.getSemester())
                .major(entity.getMajor())
                .doubleMajor(entity.getDoubleMajor())
                .build();
    }
}
