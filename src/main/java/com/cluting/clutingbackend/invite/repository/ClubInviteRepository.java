package com.cluting.clutingbackend.invite.repository;

import com.cluting.clutingbackend.plan.domain.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubInviteRepository extends JpaRepository<Club, Long> {

}