package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;

import java.util.Set;

@Getter
@Setter
@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private State state;

    @Column(nullable = true)
    private Integer score;  //모든 운영진 평가 점수의 평균

    @Column(nullable = true)
    private Integer numClubUser;  //평가한 운영진의 수

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(length = 100, nullable = true)
    private String part;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    public enum State {
        BEFORE, INPROCESS, DOCPASS, DOCFAIL, AFTER, COMPLETED, OBJECTION, RESOLVED, PASS, FAIL
    }

}
