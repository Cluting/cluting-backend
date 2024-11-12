package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.Club;
import com.cluting.clutingbackend.plan.domain.Part;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Long> {
}
