package com.cluting.clutingbackend.plan.dto.response;

import com.cluting.clutingbackend.plan.domain.enums.Role;
import com.cluting.clutingbackend.plan.domain.enums.Semester;
import com.cluting.clutingbackend.plan.domain.User;
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
