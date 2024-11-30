package com.cluting.clutingbackend.plan.domain;


import com.cluting.clutingbackend.recruit.domain.Recruit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recruit_id", nullable = false)
    private Recruit recruit;

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

