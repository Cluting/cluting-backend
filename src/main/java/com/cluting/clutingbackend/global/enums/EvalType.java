package com.cluting.clutingbackend.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EvalType {
    DOCUMENT("서류"),
    INTERVIEW("면접");

    private final String description;
}