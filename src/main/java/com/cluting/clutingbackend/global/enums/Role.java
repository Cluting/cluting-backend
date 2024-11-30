package com.cluting.clutingbackend.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("관리자"),
    GENERAL("사용자");

    private final String description;
}
