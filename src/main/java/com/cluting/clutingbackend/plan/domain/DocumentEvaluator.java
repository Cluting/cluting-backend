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
@Table(name = "tb_document_evaluator")
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
    @JoinColumn(name = "group_id", nullable = true)
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    @Builder.Default
    private Stage stage = Stage.READABLE;  // 기본 상태는 열람가능 상태(본인 담당인 경우에 BEFORE 상태가 됨.)

    @Column
    private Integer score; // 평가 점수

    @Column
    private String comment; // 평가 코멘트

    public static DocumentEvaluator of(
            ClubUser clubUser,
            Application application,
            Group group,
            Stage stage,
            Integer score,
            String comment
    ) {
        return DocumentEvaluator.builder()
                .clubUser(clubUser)
                .application(application)
                .group(group)
                .stage(stage)
                .score(score)
                .comment(comment)
                .build();
    }

}

