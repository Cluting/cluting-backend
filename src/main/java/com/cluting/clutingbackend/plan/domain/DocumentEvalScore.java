package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DocumentEvalScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int score;

    @ManyToOne
    @JoinColumn(name = "documentCriteria_id", nullable = false)
    private DocumentCriteria documentCriteria;

    @ManyToOne
    @JoinColumn(name = "documentEvaluator_id", nullable = false)
    private DocumentEvaluator documentEvaluator;

}

