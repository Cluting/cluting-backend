package com.cluting.clutingbackend.recruit.repository;

import com.cluting.clutingbackend.recruit.domain.RecruitSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitScheduleRepository extends JpaRepository<RecruitSchedule, Long> {
}
