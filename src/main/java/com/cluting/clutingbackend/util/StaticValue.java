package com.cluting.clutingbackend.util;

public class StaticValue {
    // JWT
    public static final Long JWT_ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 30; // 30분
    public static final Long JWT_REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 7; // 7일
}
