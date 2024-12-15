package com.cluting.clutingbackend.interview.domain;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.global.enums.EvaluateStatus;
import com.cluting.clutingbackend.global.enums.Stage;
import com.cluting.clutingbackend.plan.domain.Group;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewEvaluator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_user_id", nullable = true)
    private ClubUser clubUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = true)
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    @Builder.Default
    private Stage stage = Stage.BEFORE;

    @Column(nullable = true)
    private Integer score;

    @Column(nullable = true)
    private String comment;

    @Column(nullable = true)
    private LocalDateTime interviewTime; //운영진과 지원자의 면접 확정 시간
}
