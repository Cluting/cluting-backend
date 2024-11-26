package com.cluting.clutingbackend.plan.domain;

import com.cluting.clutingbackend.part.Part;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @OneToMany(mappedBy = "post")
    private List<TimeSlot> timeSlots;

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
    private String description; // 내용

    @Column(length = 255, nullable = true)
    private String profile; //프로필

    @Column(nullable = true)
    private Boolean isDone; // 마감여부

    @Column(length = 255, nullable = true)
    private String caution; // 공고 질문 관련 주의 사항

    @Column(nullable = true)
    private LocalDateTime deadLine; // 마감기한

    @Column(nullable = true)
    private LocalDateTime createdAt; // 생성일

    @Column
    private String applicationTitle; //지원서 제목

    @Column
    private Integer interviewerCount; // 면접관 수

    @Column
    private Integer intervieweeCount; // 면접자 수

    @Column
    private Integer interviewDuration; // 면접 소요 시간

    @Column
    private boolean isRequiredPortfolio; // 면접 소요 시간


    @OneToMany(mappedBy = "partId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Part> parts;

    public enum CurrentStage {
        PREP, PLAN, DOC, DOC_PASS, EVAL, FINAL_PASS
    }
}
