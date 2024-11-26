package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import com.cluting.clutingbackend.part.Part;

@Entity
public class Criteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long criteriaId;

    @ManyToOne
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @ManyToOne
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;

    @Column(length = 255, nullable = true)
    private String content;

    // Getters and Setters
}

