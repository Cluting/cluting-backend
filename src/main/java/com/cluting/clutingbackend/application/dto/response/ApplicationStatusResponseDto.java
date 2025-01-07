package com.cluting.clutingbackend.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationStatusResponseDto {
    // 지원 현황
    private String clubName; // 동아리 이름
    private String clubProfile; // 동아리 로고
    private RecruitStatus status; // 지원상태

    // 지원 캘린더
    private LocalDate recruitmentStartDate; // 모집 시작일
    private LocalDate recruitmentEndDate; // 모집 종료일

    private LocalDate documentResultDate; // 서류 합격자 발표일
    private LocalDate finalResultDate; // 최종 합격자 발표일

}
