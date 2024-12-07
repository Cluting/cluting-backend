package com.cluting.clutingbackend.admininvite.dto;

import lombok.Getter;
import lombok.Setter;

// [운영진 초대] 초대 링크 생성 시, clubId를 기반으로 토큰을 생성함.
@Getter
@Setter
public class AdminInviteRequestDto {
    private Long clubId;
}