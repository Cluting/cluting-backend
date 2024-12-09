package com.cluting.clutingbackend.recruit.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tb_recruitschedule")
public class RecruitSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_id", nullable = false)
    private Recruit recruit;

    @Column(name = "stage_1_end", nullable = false)
    private LocalDate stage1End;

    @Column(name = "stage_1_start", nullable = false)
    private LocalDate stage1Start;

    @Column(name = "stage_2_start", nullable = false)
    private LocalDate stage2Start;

    @Column(name = "stage_2_end", nullable = false)
    private LocalDate stage2End;

    @Column(name = "stage_3_start", nullable = false)
    private LocalDate stage3Start; // 모집기간 시작일

    @Column(name = "stage_3_end", nullable = false)
    private LocalDate stage3End; // 모집기간 종료일

    @Column(name = "stage_4_start", nullable = false)
    private LocalDate stage4Start;

    @Column(name = "stage_4_end", nullable = false)
    private LocalDate stage4End;

    @Column(name = "stage_5_start", nullable = false)
    private LocalDate stage5Start;

    @Column(name = "stage_5_end", nullable = false)
    private LocalDate stage5End;

    @Column(name = "stage_6_start", nullable = false)
    private LocalDate stage6Start;

    @Column(name = "stage_6_end", nullable = false)
    private LocalDate stage6End;

    @Column(name = "stage_7_start", nullable = false)
    private LocalDate stage7Start;

    @Column(name = "stage_7_end", nullable = false)
    private LocalDate stage7End;

    @Column(name = "stage_8_start", nullable = false)
    private LocalDate stage8Start;

    @Column(name = "stage_8_end", nullable = false)
    private LocalDate stage8End;

    @Column(name = "interview_location", nullable = true)
    private String interviewLocation;
}
