package com.cluting.clutingbackend.interview.domain;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
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
}
