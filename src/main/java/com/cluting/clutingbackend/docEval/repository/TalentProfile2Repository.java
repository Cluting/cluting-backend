package com.cluting.clutingbackend.docEval.repository;

import com.cluting.clutingbackend.plan.domain.TalentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TalentProfile2Repository extends JpaRepository<TalentProfile, Long> {
    @Query("SELECT tp FROM TalentProfile tp WHERE tp.part.id = :partId")
    List<TalentProfile> findByPartId(Long partId);
}