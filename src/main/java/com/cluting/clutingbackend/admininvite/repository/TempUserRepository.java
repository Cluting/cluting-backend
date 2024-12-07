package com.cluting.clutingbackend.admininvite.repository;

import com.cluting.clutingbackend.admininvite.domain.TempUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TempUserRepository extends JpaRepository<TempUser, Long> {
    Optional<TempUser> findByEmail(String email);
}
