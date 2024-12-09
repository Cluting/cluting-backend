package com.cluting.clutingbackend.user.domain;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.global.enums.Role;
import com.cluting.clutingbackend.global.enums.Semester;
import com.cluting.clutingbackend.global.enums.StudentStatus;
import com.cluting.clutingbackend.todo.domain.Todo;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = true)
    private String email;

    @Column(length = 100, nullable = true)
    private String password;

    @Column(length = 100, nullable = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Role role;

    @Column(length = 15, nullable = true)
    private String phone;

    @Column(length = 100, nullable = true)
    private String location;

    @Column(length = 100, nullable = true)
    private String school;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private StudentStatus studentStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Semester semester;

    @Column(length = 100, nullable = true)
    private String major;

    @Column(length = 100, nullable = true)
    private String doubleMajor;

    @Column(length = 255, nullable = true)
    private String profile;

    @Column(nullable = false)
    private Boolean termsOfService; // 클루팅 이용약관 동의 여부 (필수)

    @Column(nullable = false)
    private Boolean privacyPolicy; // 개인정보 수집 및 이용 동의 여부 (필수)

    @Column(nullable = true)
    private Boolean marketingConsent; // 마케팅 이벤트 메일 수신 동의 여부 (선택)

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClubUser> clubUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Todo> todoList;
}
