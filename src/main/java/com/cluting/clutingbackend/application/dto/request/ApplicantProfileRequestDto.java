package com.cluting.clutingbackend.application.dto.request;

import com.cluting.clutingbackend.global.enums.Semester;
import com.cluting.clutingbackend.global.enums.StudentStatus;
import lombok.Data;

@Data
public class ApplicantProfileRequestDto {
    private String name; // 이름
    private String phoneNum; //전화번호
    private String addr; // 주소
    private String university; // 대학
    private String major; // 전공
    private String doubleMajor; // 다전공
    private StudentStatus studentStatus; //재학/휴학
    private Semester semester; //학기?

}
