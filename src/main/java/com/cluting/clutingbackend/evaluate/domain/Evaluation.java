package com.cluting.clutingbackend.evaluate.domain;

import com.cluting.clutingbackend.plan.domain.Application;
import com.cluting.clutingbackend.plan.domain.ClubUser;
import com.cluting.clutingbackend.plan.domain.Interview;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evaluationId;

    @ManyToOne
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @ManyToOne
    @JoinColumn(name = "clubUser_id", nullable = false)
    private ClubUser clubUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Stage stage;

    @Column(nullable = true)
    private Integer score;  //운영진별 평기기준 점수들의 합

    @Column(length = 500, nullable = true)
    private String comment;

    public enum Stage {
        DOCUMENT, INTERVIEW
    }

}

