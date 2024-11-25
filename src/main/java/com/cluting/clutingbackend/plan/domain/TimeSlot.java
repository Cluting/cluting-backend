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
    private LocalDateTime times; // 시간대 (예: "10월 20일-09:00")

    @Column
    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name="clubUser_id")
    private ClubUser clubUser;


    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

}
