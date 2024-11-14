package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tb_post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clubId", nullable = false)
    private Club club;

    @Column(length = 100, nullable = true)
    private String title;

    @Column(nullable = true)
    private String description;

    @Column(length = 255, nullable = true)
    private String profile;

    @Column(nullable = true)
    private Boolean isDone;

    @Column(length = 255, nullable = true)
    private String caution;

    @Column(nullable = true)
    private LocalDateTime deadLine;

    @Column(nullable = true)
    private LocalDateTime createdAt;
}
