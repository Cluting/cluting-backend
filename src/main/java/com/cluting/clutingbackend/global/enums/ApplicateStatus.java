package com.cluting.clutingbackend.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicateStatus {
    A("지원완료"),
    B("서류평가중"),
    C("서류평가완료"),
    D("면접평가중"),
    E("최종합격자발표");

    private final String description;
}

