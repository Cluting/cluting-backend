package com.cluting.clutingbackend.plan.domain;


import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.global.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class DocumentAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private DocumentQuestion documentQuestion;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column
    private String content; // 내용

}
