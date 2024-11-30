package com.cluting.clutingbackend.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EvaluateStatus {
    BEFORE("평가 전"),

    DOCINPROCESS("서류평가 중"),
    INTERVIEWINPROCESS("면접평가 중"),

    DOCPASS("서류 합격"),
    DOCFAIL("서류 불합격"),

    COMPLETED("평가 완료"),
    OBJECTION("이의제기"),
    RESOLVED("이의해결"),

    PASS("최종 합격"),
    FAIL("최종 불합격");

    private final String description;
}
