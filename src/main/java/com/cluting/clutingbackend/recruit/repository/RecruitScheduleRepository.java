package com.cluting.clutingbackend.recruit.repository;

import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.recruit.domain.RecruitSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruitScheduleRepository extends JpaRepository<RecruitSchedule, Long> {

    Optional<RecruitSchedule> findByRecruitId(Long recruitId);

}
