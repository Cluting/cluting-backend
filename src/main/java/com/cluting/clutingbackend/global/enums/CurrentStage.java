package com.cluting.clutingbackend.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CurrentStage {
    PREP("계획 세우기"),
    PLAN("모집 준비하기"),
    DOC("서류 평가하기"),
    DOC_PASS("서류합격자 및 면접안내"),
    EVAL("면접 평가하기"),
    FINAL_PASS("최종합격자 및 활동안내");

    private final String description;
}
