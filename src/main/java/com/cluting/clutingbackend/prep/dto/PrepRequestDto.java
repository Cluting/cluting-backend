package com.cluting.clutingbackend.prep.dto;

import com.cluting.clutingbackend.recruit.dto.RecruitScheduleDto;
import lombok.*;

import java.util.List;

// [계획하기] 등록하기 DTO
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepRequestDto {
    private List<RecruitScheduleDto> recruitSchedules; // 리크루팅 일정
    private List<PrepStageDto> prepStages; // 모집 단계별 운영진
    private List<String> applicantGroups; // 지원자 그룹
}