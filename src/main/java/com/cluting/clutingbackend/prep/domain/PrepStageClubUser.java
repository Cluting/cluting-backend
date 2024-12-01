package com.cluting.clutingbackend.prep.domain;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepStageClubUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prep_stage_id", nullable = false)
    private PrepStage prepStage;

    @ManyToOne
    @JoinColumn(name = "club_user_id", nullable = false)
    private ClubUser clubUser;
}
