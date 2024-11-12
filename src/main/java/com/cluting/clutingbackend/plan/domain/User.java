package com.cluting.clutingbackend.plan.domain;

import com.cluting.clutingbackend.plan.domain.enums.Role;
import com.cluting.clutingbackend.plan.domain.enums.Semester;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tb_user")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String name;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String phone;
    private String location;
    private String school;
    private Boolean isOnLeaver;
    @Enumerated(EnumType.STRING)
    private Semester semester;
    private String major;
    private String doubleMajor;
    private String profile;
}
