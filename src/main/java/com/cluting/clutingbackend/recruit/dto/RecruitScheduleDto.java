package com.cluting.clutingbackend.recruit.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// [리크루팅 홈] 리크루팅 일정 GET
public class RecruitScheduleDto {
    private LocalDate stage1Start; //리크루팅 준비 기간(서류까지만 리크루팅 하는 경우)
    private LocalDate stage1End;
    private LocalDate stage2Start;  //공고 업로드
    private LocalDate stage2End;
    private LocalDate stage3Start;  //모집기간(서류까지만 리크루팅 하는 경우)
    private LocalDate stage3End;
    private LocalDate stage4Start;  //서류평가기간(서류까지만 리크루팅 하는 경우)
    private LocalDate stage4End;
    private LocalDate stage5Start;  //1차 합격자 발표
    private LocalDate stage5End;
    private LocalDate stage6Start;  //면접 기간
    private LocalDate stage6End;
    private LocalDate stage7Start;  //면접 평가 기간
    private LocalDate stage7End;
    private LocalDate stage8Start;  //최종 합격자 발표(서류까지만 리크루팅 하는 경우)
    private LocalDate stage8End;
}
