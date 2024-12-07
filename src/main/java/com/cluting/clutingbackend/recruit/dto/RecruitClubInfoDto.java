package com.cluting.clutingbackend.recruit.dto;

import com.cluting.clutingbackend.global.enums.CurrentStage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// [리크루팅 홈] 동아리 정보 GET
public class RecruitClubInfoDto {
    private String clubName;  //동아리 이름
    private String clubProfile;  //동아리 프로필
    private Boolean isInterview;  //면접까지 진행하는지, 서류까지만 진행하는지 -> 이에 따라 메뉴가 달라짐
    private Integer generation;  //기수
    private CurrentStage currentStage;  //현재 진행중인 리크루팅 단계
}
