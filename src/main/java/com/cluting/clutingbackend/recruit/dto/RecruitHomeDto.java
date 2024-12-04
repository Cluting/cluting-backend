package com.cluting.clutingbackend.recruit.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
// [리크루팅 홈] 불러오기
public class RecruitHomeDto {
    private RecruitClubInfoDto recruitInfo;
    private RecruitScheduleDto recruitSchedule;
    private List<ClubUserInfoDto> adminList;
    private List<TodoDto> userTodos;
}
