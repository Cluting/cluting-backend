package com.cluting.clutingbackend.global.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Stage {
    BEFORE("평가 전"),
    ING("평가 중"),
    AFTER("평가 완료");

    private final String description;
}
