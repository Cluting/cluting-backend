package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
//@Table(name = "tb_club")
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "club")
    private List<Post> posts;

    @Column(length = 100, nullable = true)
    private String name; // 동아리 이름

    @Column(nullable = true)
    private String description; // 소개

    @Column(length = 255, nullable = true)
    private String profile; // 프로필사진

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Category category; // 동아리 분야

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Type type; // 연합동아리,교내동아리

    @Column(length = 100, nullable = true)
    private String keyword; // 키워드

    @Setter
    @Column(nullable = true)
    private Boolean isRecruiting; //리크루팅시작여부

    @Builder.Default
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClubUser> clubUsers = new ArrayList<>();

    public enum Category {
        CULTURE, PHYSICAL, STARTUP, LANGUAGE, SOCIAL, TECHNOLOGY, SERVICE, ACADEMIC, ELSE
    }

    public enum Type {
        INTERNAL, EXTERNAL
    }

    public void addUser(ClubUser clubUser) {
        this.clubUsers.add(clubUser);
    }

    public void delUser(ClubUser clubUser) {
        this.clubUsers.remove(clubUser);
    }
}
