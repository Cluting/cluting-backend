package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.ClubUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubUserRepository extends JpaRepository<ClubUser,Long> {

}
