package com.cluting.clutingbackend.plan.domain;

import com.cluting.clutingbackend.global.enums.EvalType;
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
    private List<Ideal> idealList;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    @Builder.Default
    private Integer numDoc = 0;

    @Column(nullable = true)
    @Builder.Default
    private Integer numFinal = 0;

    @Column(nullable = true)
    @Builder.Default
    private Integer numRecruit = 0;

    @Column(nullable = true)
    private String warning;

    @Enumerated(EnumType.STRING)
    private EvalType evalType;

    @Column(nullable = true)
    @Builder.Default
    private boolean isCommon = false; //공통인지 아닌지

    public static Group of(
            Recruit recruit,
            String name,
            Integer numDoc,
            Integer numFinal,
            Integer numRecruit,
            String warning,
            EvalType evalType,
            Boolean isCommon
    ) {
        return Group.builder()
                .recruit(recruit)
                .name(name)
                .numDoc(numDoc)
                .numFinal(numFinal)
                .numRecruit(numRecruit)
                .warning(warning)
                .evalType(evalType)
                .isCommon(isCommon)
                .build();
    }
}

