package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "documentQuestion_id", nullable = false)
    private DocumentQuestion documentQuestion;

    @Column
    private String content; //

    @Column
    private boolean isCorrect; // 정답인지/아닌지?
}

