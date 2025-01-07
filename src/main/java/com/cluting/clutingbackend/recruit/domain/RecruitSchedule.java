package com.cluting.clutingbackend.recruit.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tb_recruit_schedule")
public class RecruitSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "recruit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Recruit recruit;

    @Column(name = "stage_1_end", nullable = true)
    private LocalDate stage1End; // 리크루팅 준비 종료일

    @Column(name = "stage_1_start", nullable = true)
    private LocalDate stage1Start; // 리크루팅 준비 시작일

    @Column(name = "stage_2_start", nullable = true)
    private LocalDate stage2Start; // 공고업로드 시작일

    @Column(name = "stage_2_end", nullable = true)
    private LocalDate stage2End; // 공고업로드 종료일

    @Column(name = "stage_3_start", nullable = false)
    private LocalDate stage3Start; // 모집기간 시작일

    @Column(name = "stage_3_end", nullable = false)
    private LocalDate stage3End; // 모집기간 종료일

    @Column(name = "stage_4_start", nullable = true)
    private LocalDate stage4Start; //서류평가기간 시작일

    @Column(name = "stage_4_end", nullable = true)
    private LocalDate stage4End; // 서류평가기간 종료일

    @Column(name = "stage_5_start", nullable = true)
    private LocalDate stage5Start; // 1차합격자발표 시작일

    @Column(name = "stage_5_end", nullable = true)
    private LocalDate stage5End; // 1차합격자발표 종료일

    @Column(name = "stage_6_start", nullable = true)
    private LocalDate stage6Start; // 면접기간 시작일

    @Column(name = "stage_6_end", nullable = true)
    private LocalDate stage6End; // 면접기간 종료일

    @Column(name = "stage_7_start", nullable = true)
    private LocalDate stage7Start; // 면접 평가기간 시작일

    @Column(name = "stage_7_end", nullable = true)
    private LocalDate stage7End; // 면접 평가기간 종료일

    @Column(name = "stage_8_start", nullable = true)
    private LocalDate stage8Start; // 최종합격자발표 시작일

    @Column(name = "stage_8_end", nullable = true)
    private LocalDate stage8End; // 최종합격자발표 종료일
}
