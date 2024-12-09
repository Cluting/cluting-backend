package com.cluting.clutingbackend.prep.domain;

import com.cluting.clutingbackend.recruit.domain.Recruit;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
