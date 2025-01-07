package com.cluting.clutingbackend.prep.domain;

import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tb_prep_stage")
public class PrepStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recruit_id", nullable = false)
    private Recruit recruit;

    @Builder.Default
    @OneToMany(mappedBy = "prepStage", cascade = CascadeType.ALL)
    private List<PrepStageClubUser> prepStageClubUser = new ArrayList<>();

    @Column(nullable = false)
    private String stageName;

    @Column(nullable = false)
    private Integer stageOrder;
}
