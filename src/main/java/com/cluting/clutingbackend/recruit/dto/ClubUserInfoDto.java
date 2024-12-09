package com.cluting.clutingbackend.recruit.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
// [리크루팅 홈] 운영진 리스트 GET
public class ClubUserInfoDto {
    private String name;
    private String email;
}
