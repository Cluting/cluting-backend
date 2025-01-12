package com.cluting.clutingbackend.plan.domain;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.global.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tb_document_answer")
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
    private String content; // 내용'

    public static DocumentAnswer of(
            DocumentQuestion documentQuestion,
            Application application,
            String content
    ) {
        return DocumentAnswer.builder()
                .documentQuestion(documentQuestion)
                .application(application)
                .content(content)
                .build();
    }

}
