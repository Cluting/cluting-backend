package com.cluting.clutingbackend.plan.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(length = 100, nullable = true)
    private String name; // 파트명

    @Column(nullable = true)
    private Integer numDoc; // 서류합격인원

    @Column(nullable = true)
    private Integer numFinal; // 최종합격인원

    @Column
    private String warning; // 파트별 주의사항

    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TalentProfile> talentProfiles; // 일대다 관계
}

