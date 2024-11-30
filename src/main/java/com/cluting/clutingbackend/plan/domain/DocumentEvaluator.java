package com.cluting.clutingbackend.plan.domain;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import jakarta.persistence.*;

@Entity
public class DocumentEvaluator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clubUser_id", nullable = false)
    private ClubUser clubUser;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @ManyToOne
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @Column
    private String stage; // 단계 -> 추후 enum으로 (전,중,후)

    @Column
    private Integer score; // 평가 점수

    @Column
    private String comment; // 평가 코멘트

}

