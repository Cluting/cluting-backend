package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private State state;

    @Column(nullable = true)
    private Integer score;  //모든 운영진 평가 점수의 평균

    @Column(nullable = true)
    private Integer numClubUser;  //평가한 운영진의 수

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    @Column(length = 100, nullable = true)
    private String part;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public enum State {
        BEFORE, INPROCESS, AFTER, COMPLETED,
        OBJECTION, RESOLVE, PASS, FAIL
    }
//    public enum State {
//        SUBMITTED, REVIEWED, APPROVED
//    }

}

