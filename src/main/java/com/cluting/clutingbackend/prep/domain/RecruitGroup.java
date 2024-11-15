package com.cluting.clutingbackend.prep.domain;

import com.cluting.clutingbackend.plan.domain.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class RecruitGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    @Column(length = 100, nullable = false)
    private String groupName; // 그룹 이름

}
