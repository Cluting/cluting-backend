package com.cluting.clutingbackend.interview.domain;


import com.cluting.clutingbackend.application.domain.Application;
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

    private Integer interviewerCount;
    private Integer duration;

    @Enumerated(EnumType.STRING)
    private Stage state;

    private Integer score;
    private Integer numClubUser;
    private String part;
}
