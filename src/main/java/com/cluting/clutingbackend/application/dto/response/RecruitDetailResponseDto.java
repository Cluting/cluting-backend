package com.cluting.clutingbackend.application.dto.response;

import java.time.LocalDate;

public class RecruitDetailResponseDto {

    private String clubName;         // 동아리명
    private String posterImageUrl;  // 홍보 포스터
    private int generation;         // 기수
    private int recruitmentNumber;  // 모집 인원
    private LocalDate startDate;    // 접수 시작일
    private LocalDate documentResultDate; // 서류 합격자 발표일
    private LocalDate finalResultDate;    // 최종 합격자 발표일
    private String activityPeriod; // 활동 기간
    private String activityDay;    // 활동 요일
    private String activityTime;   // 활동 시간
    private int clubFee;           // 동아리 회비
    private String description;    // 세부 내용

    public RecruitDetailResponseDto(String clubName, String posterImageUrl, int generation,
                                        int recruitmentNumber, LocalDate startDate, LocalDate documentResultDate,
                                        LocalDate finalResultDate, String activityPeriod, String activityDay,
                                        String activityTime, int clubFee, String description) {
        this.clubName = clubName;
        this.posterImageUrl = posterImageUrl;
        this.generation = generation;
        this.recruitmentNumber = recruitmentNumber;
        this.startDate = startDate;
        this.documentResultDate = documentResultDate;
        this.finalResultDate = finalResultDate;
        this.activityPeriod = activityPeriod;
        this.activityDay = activityDay;
        this.activityTime = activityTime;
        this.clubFee = clubFee;
        this.description = description;
    }

    // Getters and Setters
}

