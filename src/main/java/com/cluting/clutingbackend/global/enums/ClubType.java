package com.cluting.clutingbackend.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClubType {
    INTERNAL("교내동아리"),
    EXTERNAL("연합동아리");

    private final String description;
}
