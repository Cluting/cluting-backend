package com.cluting.clutingbackend.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    CULTURE("문화/예술/공연"),
    PHYSICAL("체육"),
    STARTUP("창업/취업"),
    LANGUAGE("어학"),
    SOCIAL("친목"),
    SERVICE("봉사/사회활동"),
    ACADEMIC("학술/교양"),
    ELSE("그 외");

    private final String description;
}
