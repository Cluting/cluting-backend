package com.cluting.clutingbackend.global.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionLevel {
    ONE("1단계"),
    TWO("2단계"),
    THREE("3단계"),
    FOUR("4단계"),
    FIVE("5단계");

    private final String description;

}
