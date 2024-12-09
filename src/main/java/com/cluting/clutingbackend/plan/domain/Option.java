package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name ="tb_option")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "document_question_id", nullable = false)
    private DocumentQuestion documentQuestion;

    @Column
    private String content; // 선지 내용

    @Column
    private boolean isCorrect; // 정답인지/아닌지?
}

