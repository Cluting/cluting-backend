package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "applicationId", nullable = false)
    private Application application;

    @Column(nullable = true)
    private Date date;

    @Column(length = 255, nullable = true)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private State state;

    @Column(nullable = false)
    private Integer interviewerCount; // 면접관 인원수

    @Column(nullable = false)
    private Integer intervieweeCount; // 면접자 인원수

    @Column(nullable = false)
    private Integer duration;
    // 30분 혹은 60분
    public enum State {
        BEFORE, INPROCESS, AFTER, COMPLETED
    }
}

