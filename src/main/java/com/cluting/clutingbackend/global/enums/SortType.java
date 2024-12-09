package com.cluting.clutingbackend.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortType {
    DEADLINE("마감임박순"),
    NEWEST("최신순"),
    OLDEST("오래된순");

    private final String description;
}
