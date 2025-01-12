package com.cluting.clutingbackend.evaluation.domain;

import com.cluting.clutingbackend.global.enums.EvalType;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tb_message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000, nullable = true)
    private String pass;

    @Column(length = 1000, nullable = true)
    private String fail;

    @Enumerated(EnumType.STRING)
    private EvalType evalType;

    @ManyToOne
    @JoinColumn(name = "recruit_id", nullable = false)
    private Recruit recruit;

    public static Message of(
            EvalType evalType,
            Recruit recruit
    ) {
        return Message.builder()
                .pass(null)
                .fail(null)
                .evalType(evalType)
                .recruit(recruit)
                .build();
    }
}
