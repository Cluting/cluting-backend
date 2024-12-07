package com.cluting.clutingbackend.global.util;

public class StaticValue {
    // JWT
    public static final Long JWT_ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 30; // 30분
    public static final Long JWT_REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 7; // 7일

    // page
    public static final Integer PAGE_DEFAULT_SIZE = 12;

    // image
    public static final String profileImage = "https://cluting.s3.ap-northeast-2.amazonaws.com/default.png";
}
