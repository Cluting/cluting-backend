package com.cluting.clutingbackend.part;


import com.cluting.clutingbackend.plan.domain.Post;
import com.cluting.clutingbackend.plan.domain.TalentProfile;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partId;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    @Column(length = 100, nullable = true)
    private String name;

    @Column(nullable = true)
    private Integer numDoc;

    @Column(nullable = true)
    private Integer numFinal;

    @Column(nullable = true)
    private Integer numRecruit;

    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TalentProfile> talentProfiles; // 일대다 관계
}

