package com.cluting.clutingbackend.prep.domain;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tb_prep_stage_club_user")
public class PrepStageClubUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prep_stage_id", nullable = false)
    @JsonBackReference
    private PrepStage prepStage;

    @ManyToOne
    @JoinColumn(name = "club_user_id", nullable = false)
    private ClubUser clubUser;
}
