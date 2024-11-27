package com.cluting.clutingbackend.docEval.repository;

import com.cluting.clutingbackend.plan.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface User2Repository extends JpaRepository<User, Long> {
    Optional<User> findByNameAndPhone(String name, String phone);
}