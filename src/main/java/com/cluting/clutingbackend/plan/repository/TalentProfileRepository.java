package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.TalentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TalentProfileRepository extends JpaRepository<TalentProfile, Long> {
}

