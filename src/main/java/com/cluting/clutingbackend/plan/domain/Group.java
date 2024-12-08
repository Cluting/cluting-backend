package com.cluting.clutingbackend.plan.domain;


import com.cluting.clutingbackend.recruit.domain.Recruit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "tb_group")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recruit_id", nullable = false)
    private Recruit recruit;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private Integer numDoc;

    @Column(nullable = true)
    private Integer numFinal;

    @Column(nullable = true)
    private Integer numRecruit;

    @Column(nullable = true)
    private String warning;

    public static Group of(
            Recruit recruit,
            String name,
            Integer numDoc,
            Integer numFinal,
            Integer numRecruit,
            String warning
    ) {
        return Group.builder()
                .recruit(recruit)
                .name(name)
                .numDoc(numDoc)
                .numFinal(numFinal)
                .numRecruit(numRecruit)
                .warning(warning)
                .build();
    }
}
