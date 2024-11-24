package com.cluting.clutingbackend.plan.domain;


import com.cluting.clutingbackend.part.Part;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class TalentProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @Column(length = 500, nullable = false)
    private String description; // 인재상 내용

    @ManyToOne
    @JoinColumn(name = "partId", nullable = false)
    private Part part; // 각 인재상은 하나의 파트에만 속함
}

