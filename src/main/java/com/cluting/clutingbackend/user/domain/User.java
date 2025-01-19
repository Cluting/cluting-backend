package com.cluting.clutingbackend.user.domain;

import com.cluting.clutingbackend.application.dto.request.ApplicantProfileRequestDto;
import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.global.enums.Role;
import com.cluting.clutingbackend.global.enums.Semester;
import com.cluting.clutingbackend.global.enums.Status;
import com.cluting.clutingbackend.global.enums.StudentStatus;
import com.cluting.clutingbackend.global.util.EncryptionUtil;
import com.cluting.clutingbackend.todo.domain.Todo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column
    private Status status;

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

    @OneToMany(mappedBy = "user")
    private List<Scrap> scrappedList;

    @Column(nullable = true)
    private String portfolioFile; // 포트폴리오 파일(pdf)

    @Column(nullable = true)
    private String portfolioUrl; // 포트폴리오 주소(notion이나 다른 링크드인 등)

    public void updateUserInfo(ApplicantProfileRequestDto dto){
        this.name= dto.getName();
        this.phone = dto.getPhoneNum();
        this.location = dto.getAddr();
        this.school = dto.getUniversity();
        this.major = dto.getMajor();
        this.doubleMajor = dto.getDoubleMajor();
        this.studentStatus = dto.getStudentStatus();
        this.semester = dto.getSemester();
    }

    public void update(String name, String phone, String location, String school, String major, String doubleMajor, StudentStatus studentStatus, Semester semester) {
        this.name = name;
        this.phone = phone;
        this.location = location;
        this.school = school;
        this.major = major;
        this.doubleMajor = doubleMajor;
        this.studentStatus = studentStatus;
        this.semester = semester;
    }

    public void update(String profile) {
        this.profile = profile;
    }

    @Override
    public String toString(){
        return String.format("[User 객체] id : %s, name : %s, email : %s, password : %s", this.id, this.name , this.email, this.password);
    }

    public void encrypt() throws Exception {
        this.setEmail(EncryptionUtil.encrypt(this.getEmail()));
        this.setPhone(EncryptionUtil.encrypt(this.getPhone()));
        this.setName(EncryptionUtil.encrypt(this.getName()));
        this.setLocation(EncryptionUtil.encrypt(this.getLocation()));
        this.setPortfolioFile(EncryptionUtil.encrypt(this.getPortfolioFile()));
        this.setPortfolioUrl(EncryptionUtil.encrypt(this.getPortfolioUrl()));
        this.setProfile(EncryptionUtil.encrypt(this.getProfile()));
    }
}
