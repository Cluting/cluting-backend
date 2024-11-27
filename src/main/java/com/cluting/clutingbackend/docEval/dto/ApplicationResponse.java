package com.cluting.clutingbackend.docEval.dto;

import com.cluting.clutingbackend.plan.domain.Application;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationResponse {
    private Application.State state; // 지원서 평가 상태
    private String applicantName;    // 지원자 이름
    private String phone;            // 전화번호
    private String part;             // 지원한 그룹
    private String evaluationStatus; // 운영진 평가 상황 (<평가한 운영진 수>/<평가 권한이 있는 운영진 수>)
}