package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;


@Entity
public class TalentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Column
    private String profile; // 인재상 내용

}
