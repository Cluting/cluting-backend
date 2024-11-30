package com.cluting.clutingbackend.recruit.domain;

import com.cluting.clutingbackend.club.domain.Club;
import com.cluting.clutingbackend.global.enums.CurrentStage;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tb_recruit")
public class Recruit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Column(length = 100, nullable = false)
    private String title; // 공고 제목

    @Lob
    @Column(length = 1000, nullable = true)
    private String description; // 내용

    @Column(length = 500, nullable = true)
    private String image; // 공고 이미지 경로

    @Column(nullable = true)
    private Boolean isDone; // 마감여부

    @Column(length = 255, nullable = true)
    private String caution; // 공고 질문 관련 주의 사항

    @Column(nullable = true)
    private LocalDateTime deadLine; // 마감기한

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private CurrentStage currentStage;  // 현재 진행중인 리크루팅 단계

    @Column(nullable = true)
    private Boolean isInterview; // true : 면접까지 진행 | false : 서류까지 진행

    @Column
    private String applicationTitle; //지원서 제목

    @Column
    private boolean isRequiredPortfolio; // 포트폴리오 필요 여부

    @Column
    private Integer interviewerCount; // 면접관 수

    @Column
    private Integer intervieweeCount; // 면접자 수

    @Column
    private Integer interviewDuration; // 면접 소요 시간
}
