package com.cluting.clutingbackend.recruit.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
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

    @Column(name = "stage_1_end", nullable = true)
    private LocalDate stage1End;

    @Column(name = "stage_1_start", nullable = true)
    private LocalDate stage1Start;

    @Column(name = "stage_2_start", nullable = true)
    private LocalDate stage2Start;

    @Column(name = "stage_2_end", nullable = true)
    private LocalDate stage2End;

    @Column(name = "stage_3_start", nullable = true)
    private LocalDate stage3Start;

    @Column(name = "stage_3_end", nullable = true)
    private LocalDate stage3End;

    @Column(name = "stage_4_start", nullable = true)
    private LocalDate stage4Start;

    @Column(name = "stage_4_end", nullable = true)
    private LocalDate stage4End;

    @Column(name = "stage_5_start", nullable = true)
    private LocalDate stage5Start;

    @Column(name = "stage_5_end", nullable = true)
    private LocalDate stage5End;

    @Column(name = "stage_6_start", nullable = true)
    private LocalDate stage6Start;

    @Column(name = "stage_6_end", nullable = true)
    private LocalDate stage6End;

    @Column(name = "stage_7_start", nullable = true)
    private LocalDate stage7Start;

    @Column(name = "stage_7_end", nullable = true)
    private LocalDate stage7End;

    @Column(name = "stage_8_start", nullable = true)
    private LocalDate stage8Start;

    @Column(name = "stage_8_end", nullable = true)
    private LocalDate stage8End;
}
