package com.cluting.clutingbackend.application.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecruitStatus {
    A("지원완료"),
    B("서류평가중"),
    C("서류평가완료"),
    D("면접평가중"),
    E("최종합격자발표");

    private final String description;
}
