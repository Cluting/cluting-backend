package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Criteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long criteriaId;

    @ManyToOne
    @JoinColumn(name = "clubUserId", nullable = true)
    private ClubUser clubUser;

    @ManyToOne
    @JoinColumn(name = "applicationId", nullable = true)
    private Application application;

    @ManyToOne
    @JoinColumn(name = "partId", nullable = false)
    private Part part;

    @ManyToOne
    @JoinColumn(name = "interviewId", nullable = true)
    private Interview interview;

    @Column(length = 255, nullable = true)
    private String content;

    @Column(length = 50, nullable = true)
    private String name;

    @Column(nullable = true)
    private Integer score;  //운영진별 평가기준별 점수
}

