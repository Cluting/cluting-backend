package com.cluting.clutingbackend.prep.repository;

import com.cluting.clutingbackend.prep.domain.PrepStageClubUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrepStageClubUserRepository extends JpaRepository<PrepStageClubUser, Long> {
    // [계획하기] 불러오기
    List<PrepStageClubUser> findByPrepStageId(Long prepStageId);
}