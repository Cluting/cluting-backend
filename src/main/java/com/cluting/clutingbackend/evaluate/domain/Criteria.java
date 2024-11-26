package com.cluting.clutingbackend.evaluate.domain;

import com.cluting.clutingbackend.plan.domain.Interview;
import com.cluting.clutingbackend.part.Part;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Criteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long criteriaId;

    @ManyToOne
    @JoinColumn(name = "partId", nullable = false)
    private Part part;

    @ManyToOne
    @JoinColumn(name = "interviewId", nullable = false)
    private Interview interview;

    @Column(length = 255, nullable = true)
    private String name;

    @Column(length = 255, nullable = true)
    private String content;

    @Column(nullable = true)
    private Integer score;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Stage stage;

    public enum Stage {
        DOCUMENT, INTERVIEW
    }
}

