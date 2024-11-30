package com.cluting.clutingbackend.recruit.repository;

import com.cluting.clutingbackend.recruit.domain.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Long> {
}
