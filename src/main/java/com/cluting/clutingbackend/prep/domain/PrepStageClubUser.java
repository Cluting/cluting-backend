package com.cluting.clutingbackend.prep.domain;

import com.cluting.clutingbackend.plan.domain.ClubUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PrepStageClubUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prepStageId", nullable = false)
    private PrepStage prepStage;

    @ManyToOne
    @JoinColumn(name = "clubUserId")
    private ClubUser clubUser;
}