package com.cluting.clutingbackend.application.domain;

import com.cluting.clutingbackend.global.enums.ApplicateStatus;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.global.enums.EvaluateStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tb_application")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private EvaluateStatus state;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicateStatus applicateStatus = ApplicateStatus.A; // 지원상태

    @Column(nullable = true)
    private Integer score;  //모든 운영진 평가 점수의 평균

    @Column(nullable = true)
    private Integer numClubUser;  //평가한 운영진의 수

    @ManyToOne
    @JoinColumn(name = "recruit_id", nullable = false)
    private Recruit recruit;

    @Column(length = 100, nullable = true)
    private String recruit_group; // 직렬화, 역직렬화 필요

    @Column
    @CreatedDate
    private LocalDateTime createdAt;
}
