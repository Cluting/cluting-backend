package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime timeSlot; // 시간대 (예: "10월 20일-09:00")

    @ElementCollection
    private List<Long> availableInterviewers; // 가능한 운영진 ID 리스트

    @ManyToOne
    @JoinColumn(name="interviewId")
    private Interview interview;

    @Builder.Default
    @Column
    private Boolean assigned = false; // 해당 시간대의 면접 배정 여부(기본값: false)
}
