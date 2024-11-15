package com.cluting.clutingbackend.prep.repository;

import com.cluting.clutingbackend.plan.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
