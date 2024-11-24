package com.cluting.clutingbackend.evaluate.domain;

import com.cluting.clutingbackend.plan.domain.ClubUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
public class Evaluator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evaluatorId;

    private String partName; // 파트 이름

    @OneToMany
    private List<ClubUser> evaluators;

}
