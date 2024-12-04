package com.cluting.clutingbackend.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EvaluateStatus {
    OBJECTION("이의제기"),
    RESOLVED("이의반영"),

    PASS("합격"),
    FAIL("불합격");

    private final String description;
}
