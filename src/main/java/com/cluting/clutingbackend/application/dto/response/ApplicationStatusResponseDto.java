package com.cluting.clutingbackend.application.dto.response;

import com.cluting.clutingbackend.global.enums.ApplicateStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationStatusResponseDto {
    // 지원 현황
    private String clubName; // 동아리 이름
    private String clubProfile; // 동아리 로고
    private ApplicateStatus status; // 지원상태
    private String aa;

    // 지원 캘린더
    private LocalDateTime recruitmentStartDate; // 모집 시작일
    private LocalDateTime recruitmentEndDate; // 모집 종료일

    private LocalDateTime documentResultDate; // 서류 합격자 발표일
    private LocalDateTime finalResultDate; // 최종 합격자 발표일

}
