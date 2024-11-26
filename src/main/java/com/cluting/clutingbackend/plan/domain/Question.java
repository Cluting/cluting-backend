package com.cluting.clutingbackend.plan.domain;


import com.cluting.clutingbackend.part.Part;
import jakarta.persistence.*;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @ManyToOne
    @JoinColumn(name = "partId", nullable = false)
    private Part part;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Type type;

    @Column(length = 255, nullable = true)
    private String content;

    @Column(nullable = true)
    private Boolean docOrInterview;  // TRUE for document, FALSE for interview

    public enum Type {
        OBJECT, SUBJECT
    }

}
