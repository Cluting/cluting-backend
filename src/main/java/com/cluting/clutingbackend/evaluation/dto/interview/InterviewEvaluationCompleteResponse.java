package com.cluting.clutingbackend.evaluation.dto.interview;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InterviewEvaluationCompleteResponse {
    private String stage; // 단계 (PASS, FAIL)
    private String applicantName; // 지원자 이름
    private String applicantPhone; // 지원자 전화번호
    private String groupName; // 그룹명
}