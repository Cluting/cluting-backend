package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class RecruitSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    @Column(nullable = true)
    private Date stage1End;

    @Column(nullable = true)
    private Date stage1Start;

    @Column(nullable = true)
    private Date stage2Start;

    @Column(nullable = true)
    private Date stage2End;

    @Column(nullable = true)
    private Date stage3Start;

    @Column(nullable = true)
    private Date stage3End;

    @Column(nullable = true)
    private Date stage4Start;

    @Column(nullable = true)
    private Date stage4End;

    @Column(nullable = true)
    private Date stage5Start;

    @Column(nullable = true)
    private Date stage5End;

    @Column(nullable = true)
    private Date stage6Start;

    @Column(nullable = true)
    private Date stage6End;

    @Column(nullable = true)
    private Date stage7Start;

    @Column(nullable = true)
    private Date stage7End;

    @Column(nullable = true)
    private Date stage8Start;

    @Column(nullable = true)
    private Date stage8End;

    // Getters and Setters
}
