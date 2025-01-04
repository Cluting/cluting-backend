package com.cluting.clutingbackend.application.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tb_applicant_interview_time_slot")
public class ApplicantInterviewTimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private boolean isAssigned;

    @Column
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;
}
