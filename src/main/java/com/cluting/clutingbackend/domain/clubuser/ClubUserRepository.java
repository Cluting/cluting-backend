package com.cluting.clutingbackend.domain.clubuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubUserRepository extends JpaRepository<ClubUser, Long> {
}
