package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Setter
@Getter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "clubId", nullable = false)
    private Club club;

    @Column(length = 100, nullable = false)
    private String title; // 공고 제목

    @Column(nullable = false)
    private LocalDateTime recruitmentStartDate; // 모집 시작일

    @Column(nullable = false)
    private LocalDateTime recruitmentEndDate; // 모집 종료일

    @Column(nullable = false)
    private LocalDateTime documentResultDate; // 서류 합격자 발표일

    @Column(nullable = false)
    private LocalDateTime finalResultDate; // 최종 합격자 발표일

    @Column(nullable = true)
    private LocalDate recruitmentNumber; // 모집 인원

    @Column(nullable = true)
    private LocalDate activityStart; // 활동 시작일

    @Column(nullable = true)
    private String activityEnd; // 활동 시작일

    @Column(length = 255, nullable = true)
    private String activitySchedule; // 활동 요일 및 시간

    @Column(nullable = true)
    private Integer clubFee; // 동아리 회비

    @Lob
    @Column(length = 1000, nullable = false)
    private String content; // 본문

}


