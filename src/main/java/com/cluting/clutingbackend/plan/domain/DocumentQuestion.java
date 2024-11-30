package com.cluting.clutingbackend.plan.domain;

import com.cluting.clutingbackend.global.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class DocumentQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @OneToOne
    @JoinColumn(name = "documentAnswer_id", nullable = false)
    private DocumentAnswer documentAnswer;

    @ManyToOne
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @Column
    private QuestionType questionType;

    @Column
    private String content;

}

