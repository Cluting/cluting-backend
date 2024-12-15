package com.cluting.clutingbackend.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IntervieweeInfoDto {
    private String name;
    private String profile;
    private String groupName;
    private String school;
    private String major;
    private String semester;
}