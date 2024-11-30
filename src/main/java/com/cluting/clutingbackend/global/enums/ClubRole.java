package com.cluting.clutingbackend.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClubRole {
    MEMBER("일반부원"),
    STAFF("운영진");

    private final String description;
}
