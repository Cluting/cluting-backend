package com.cluting.clutingbackend.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Semester {
    S1_1("1학년 1학기"),
    S1_2("1학년 2학기"),
    S2_1("2학년 1학기"),
    S2_2("2학년 2학기"),
    S3_1("3학년 1학기"),
    S3_2("3학년 2학기"),
    S4_1("4학년 1학기"),
    S4_2("4학년 2학기"),
    ELSE("그 외");

    private final String description;
}
