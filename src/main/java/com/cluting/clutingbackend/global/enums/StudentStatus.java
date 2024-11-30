package com.cluting.clutingbackend.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudentStatus {
    ENROLLED("재학"),
    LEAVE_OR_ABSENCE("휴학");

    private final String description;
}
