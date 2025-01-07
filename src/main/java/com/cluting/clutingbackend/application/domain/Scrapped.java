package com.cluting.clutingbackend.application.domain;

import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.user.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_scrapped")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ScrapId.class)
public class Scrapped {

    @Id
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "recruit_id",nullable = false)
    private Recruit recruit;

}
