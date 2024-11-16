package com.cluting.clutingbackend.recruitingHome.repository;

import com.cluting.clutingbackend.plan.domain.ClubUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubUserRecruitingHomeRepository extends JpaRepository<ClubUser, Long> {
    List<ClubUser> findByClubId(Long clubId);

}
