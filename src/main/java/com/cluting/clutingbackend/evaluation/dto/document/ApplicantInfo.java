package com.cluting.clutingbackend.evaluation.dto.document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class ApplicantInfo {
    private String name;
    private String email;
    private String phone;
    private String location;
    private String profile;
    private String school;
    private String major;
    private String doubleMajor;
    private String semester;
    private String groupName;
}
