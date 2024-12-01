package com.cluting.clutingbackend.interview.domain;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.global.enums.EvaluateStatus;
import com.cluting.clutingbackend.global.enums.Stage;
import com.cluting.clutingbackend.plan.domain.Part;
import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn(name = "club_user_id", nullable = false)
    private ClubUser clubUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = true)
    private Part part;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    @Builder.Default
    private Stage stage = Stage.BEFORE;  //각 운영진이 평가 전/중/후인지

    @Column(nullable = true)
    private Integer score;


    @Column(nullable = true)
    private String comment;
}
