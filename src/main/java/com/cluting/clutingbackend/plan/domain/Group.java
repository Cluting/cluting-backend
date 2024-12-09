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
@Table(name="tb_group")
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
    private String name; // 그룹명

    @Column
    private Integer numDoc; // 서류합격인원

    @Column
    private Integer numFinal; // 최종합격인원

    @Column
    private Integer numRecruit; // 지원자 수

    @Column
    private String warning; //주의사항

}

