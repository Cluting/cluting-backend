package com.cluting.clutingbackend.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum QuestionType {
    OBJECT("객관형"),
    SUBJECT("주관형");

    private final String description;
}



