package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;

@Entity
public class DocumentCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    // Getter, Setter, Constructor
}

