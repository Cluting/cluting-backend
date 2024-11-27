package com.cluting.clutingbackend.evaluate.domain;

import com.cluting.clutingbackend.plan.domain.Application;
import com.cluting.clutingbackend.plan.domain.ClubUser;
import com.cluting.clutingbackend.plan.domain.Interview;
import com.cluting.clutingbackend.part.Part;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Criteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long criteriaId;

    @ManyToOne
    @JoinColumn(name = "clubUserId", nullable = true)
    private ClubUser clubUser;

    @ManyToOne
    @JoinColumn(name = "applicationId", nullable = true)
    private Application application;

    @ManyToOne
    @JoinColumn(name = "partId", nullable = false)
    private Part part;

    @ManyToOne
    @JoinColumn(name = "interviewId", nullable = true)
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

