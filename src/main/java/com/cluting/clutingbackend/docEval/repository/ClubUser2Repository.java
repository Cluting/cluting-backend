package com.cluting.clutingbackend.docEval.repository;

import com.cluting.clutingbackend.plan.domain.ClubUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubUser2Repository extends JpaRepository<ClubUser, Long> {
    Optional<ClubUser> findById(Long clubUserId);
}