package com.cluting.clutingbackend.plan.domain;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.global.enums.Stage;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentEvaluator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clubUser_id", nullable = true)
    private ClubUser clubUser;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @ManyToOne
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    @Builder.Default
    private Stage stage = Stage.BEFORE;

    @Column
    private Integer score; // 평가 점수

    @Column
    private String comment; // 평가 코멘트

}

