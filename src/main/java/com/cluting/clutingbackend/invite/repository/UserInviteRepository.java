package com.cluting.clutingbackend.invite.repository;

import com.cluting.clutingbackend.plan.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInviteRepository extends JpaRepository<User, Long> {

}