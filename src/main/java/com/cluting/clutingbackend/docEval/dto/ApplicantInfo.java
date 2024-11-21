package com.cluting.clutingbackend.docEval.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicantInfo {
    private String name;
    private String phone;
    private String location;
    private String school;
    private String major;
    private String doubleMajor;
    private String semester;
    private String profile;
}