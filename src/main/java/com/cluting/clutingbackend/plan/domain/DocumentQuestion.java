package com.cluting.clutingbackend.plan.domain;

import com.cluting.clutingbackend.global.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
public class DocumentQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "documentQuestion")
    private List<DocumentAnswer> documentAnswerList;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @OneToMany(mappedBy = "documentQuestion")
    private List<Option> optionList;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Column
    private String content;

}

