package com.cluting.clutingbackend.docEval.domain;

import com.cluting.clutingbackend.plan.domain.Application;
import com.cluting.clutingbackend.plan.domain.ClubUser;
import com.cluting.clutingbackend.plan.domain.Criteria;
import com.cluting.clutingbackend.plan.domain.Part;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Evaluator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evaluatorId;

    @ManyToOne
    @JoinColumn(name = "clubUserId", nullable = false)
    private ClubUser clubUser;

    @ManyToOne
    @JoinColumn(name = "partId", nullable = false)
    private Part part;

    @Column(length = 100, nullable = true)
    private String partName;

}
