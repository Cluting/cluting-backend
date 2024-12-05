package com.cluting.clutingbackend.admininvite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// [운영진 초대] 초대 수락 시, 동아리 이름과 기수를 보여줌.
@Getter
@AllArgsConstructor
public class AdminInviteResponseDto {
    private String clubName;
    private Integer generation;
}