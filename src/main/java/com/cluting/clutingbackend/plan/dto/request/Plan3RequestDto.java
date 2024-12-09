package com.cluting.clutingbackend.plan.dto.request;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Plan3RequestDto {

    private String title;                 // 공고 제목
    //모집기간
    private LocalDate recruitmentStartDate; // 모집 시작일
    private LocalDate recruitmentEndDate;   // 모집 종료일

    private LocalDate documentResultDate;   // 서류 합격자 발표일
    private LocalDate finalResultDate;      // 최종 합격자 발표일

    private Integer recruitmentNumber;          // 모집 인원
    private LocalDate activityStart;            // 활동 시작일
    private LocalDate activityEnd;              // 활동 종료일
    private String activityDay;            // 활동 요일
    private String activityTime;            // 활동 시간대
    private Integer clubFee;                    // 동아리 회비
    private String content;                     // 본문

    private String imageUrl;                    // 모집 포스터 (사진 URL) -> s3에 링크로 저장할 예정
}

