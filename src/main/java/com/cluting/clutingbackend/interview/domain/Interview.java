package com.cluting.clutingbackend.interview.domain;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.global.enums.EvaluateStatus;
import com.cluting.clutingbackend.global.enums.Stage;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private EvaluateStatus state; // 면접 상태

    @Column(nullable = true)
    private Integer score; //모든 운영진 평가 점수의 평균

    @Column(nullable = true)
    private Integer numClubUser; //평가한 운영진의 수

    @Column(length = 100, nullable = true)
    private String recruit_group; // 직렬화, 역직렬화 필요
}
