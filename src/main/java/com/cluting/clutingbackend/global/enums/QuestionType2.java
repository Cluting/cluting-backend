package com.cluting.clutingbackend.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionType2 {
    PERSONAL("개인"),
    COMMON("공통"),
    PART_SPECIFIC("파트");

    private final String description;
}
