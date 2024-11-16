package com.cluting.clutingbackend.prep.domain;

import com.cluting.clutingbackend.plan.domain.ClubUser;
import com.cluting.clutingbackend.plan.domain.Post;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
public class PrepStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prepStageId;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    @OneToMany(mappedBy = "prepStage")
    private List<PrepStageClubUser> prepStageClubUsers;

    @Column(length = 100, nullable = false)
    private String stageName; // 단계 이름

    @Column(nullable = false)
    private int stageOrder; // 단계 순서
}
