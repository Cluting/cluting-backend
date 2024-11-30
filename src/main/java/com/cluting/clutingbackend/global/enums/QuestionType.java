package com.cluting.clutingbackend.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.service.annotation.GetExchange;

@Getter
@RequiredArgsConstructor
public enum QuestionType {
    OBJECT("객관형"),
    SUBJECT("주관형");

    private final String description;
}
