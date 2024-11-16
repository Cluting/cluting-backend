package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tb_post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clubId", nullable = false)
    private Club club;

    @Column(length = 500, nullable = true)
    private String imageUrl; // 공고 이미지 경로

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
    private Integer recruitmentNumber; // 모집 인원

    @Column(nullable = true)
    private LocalDate activityStart; // 활동 시작일

    @Column(nullable = true)
    private LocalDate activityEnd; // 활동 시작일

    @Column(length = 255, nullable = true)
    private String activitySchedule; // 활동 요일 및 시간

    @Column(nullable = true)
    private Integer clubFee; // 동아리 회비

    @Lob
    @Column(length = 1000, nullable = false)
    private String content; // 본문

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private CurrentStage currentStage;  // 현재 진행중인 리크루팅 단계

    @Column(nullable = true)
    private Boolean isInterview; // true : 면접까지 진행 | false : 서류까지 진행

    @Column(nullable = true)
    private Integer generation; // 동아리 기수

    @Column(nullable = true)
    private String description;

    @Column(length = 255, nullable = true)
    private String profile;

    @Column(nullable = true)
    private Boolean isDone;

    @Column(length = 255, nullable = true)
    private String caution;

    @Column(nullable = true)
    private LocalDateTime deadLine;

    @Column(nullable = true)
    private LocalDateTime createdAt;

    public enum CurrentStage {
        PREP, PLAN, DOC, DOC_PASS, EVAL, FINAL_PASS
    }
}
