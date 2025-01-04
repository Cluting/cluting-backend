package com.cluting.clutingbackend.user.dto.request;

import com.cluting.clutingbackend.global.enums.Semester;
import com.cluting.clutingbackend.global.enums.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {
    private String name;
    private String phone;
    private String location;

    private String school;
    private String major;
    private String doubleMajor;
    private StudentStatus studentStatus;
    private Semester semester;
}
