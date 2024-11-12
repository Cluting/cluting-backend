package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tb_club")
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = true)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(length = 255, nullable = true)
    private String profile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Type type;

    @Column(length = 100, nullable = true)
    private String keyword;

    @Column(nullable = true)
    private Boolean isRecruiting;

    public enum Category {
        CULTURE, PHYSICAL, STARTUP, LANGUAGE, SOCIAL, TECHNOLOGY, SERVICE, ACADEMIC, ELSE
    }

    public enum Type {
        INTERNAL, EXTERNAL
    }
}
