package com.cluting.clutingbackend.plan.domain;

import com.cluting.clutingbackend.global.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class DocumentQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "document_answer_id", nullable = false)
    private DocumentAnswer documentAnswer;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Column
    private String content;

}

