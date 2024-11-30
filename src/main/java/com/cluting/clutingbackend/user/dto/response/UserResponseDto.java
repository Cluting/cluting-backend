package com.cluting.clutingbackend.user.dto.response;

import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.global.enums.Role;
import com.cluting.clutingbackend.global.enums.Semester;
import com.cluting.clutingbackend.global.enums.StudentStatus;
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
    private StudentStatus studentStatus;
    private Semester semester;
    private String major;
    private String doubleMajor;
    private String profile;
//    private List<ClubUserResponseDto> clubs;

    public static UserResponseDto toDto(User entity) {
        return UserResponseDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .name(entity.getName())
                .role(entity.getRole())
                .phone(entity.getPhone())
                .location(entity.getLocation())
                .school(entity.getSchool())
                .studentStatus(entity.getStudentStatus())
                .semester(entity.getSemester())
                .major(entity.getMajor())
                .doubleMajor(entity.getDoubleMajor())
//                .clubs(entity.getClubUsers().stream().map(ClubUserResponseDto::toDto).toList())
                .build();
    }
}
