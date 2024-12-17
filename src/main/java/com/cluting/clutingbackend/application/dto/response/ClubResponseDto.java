package com.cluting.clutingbackend.application.dto.response;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ClubResponseDto {
    private String recruitName; // 공고명
    private String clubName; // 동아리명
    private String keywords; // 동아리 키워드
    private LocalDate recruitEndDate; // 모집마감일
//    private LocalDate lastViewDate; // 최근 본 날짜
    public ClubResponseDto(Application application) {
        this.recruitName = application.getRecruit().getTitle();
        this.clubName = application.getRecruit().getClub().getName();
        this.keywords = application.getRecruit().getClub().getKeyword();
        this.recruitEndDate = application.getRecruit().getRecruitSchedule().getStage3End();
    }

    public ClubResponseDto(Recruit recruit) {
        this.recruitName = recruit.getTitle();
        this.clubName = recruit.getClub().getName();
        this.keywords = recruit.getClub().getKeyword();
        this.recruitEndDate = recruit.getRecruitSchedule().getStage3End();
    }
}
