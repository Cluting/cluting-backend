package com.cluting.clutingbackend.invite.repository;

import com.cluting.clutingbackend.plan.domain.ClubUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubUserInviteRepository extends JpaRepository<ClubUser, Long> {

}
