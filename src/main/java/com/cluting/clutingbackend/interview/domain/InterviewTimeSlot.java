package com.cluting.clutingbackend.interview.domain;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tb_interview_time_slot")
public class InterviewTimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private boolean isAssigned;

    @Column
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name="clubUser_id")
    private ClubUser clubUser;

    @ManyToOne
    @JoinColumn(name="recruit_id")
    private Recruit recruit;
}
