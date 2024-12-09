package com.cluting.clutingbackend.plan.domain;


import com.cluting.clutingbackend.recruit.domain.Recruit;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recruit_id", nullable = false)
    private Recruit recruit;

    @OneToMany(mappedBy = "group")
    private List<TalentProfile> talentProfileList;

    @Column
    private String name;

    @Column
    private int numDoc;

    @Column
    private int numFinal;

    @Column
    private int numRecruit;

    @Column
    private String warning;

}

