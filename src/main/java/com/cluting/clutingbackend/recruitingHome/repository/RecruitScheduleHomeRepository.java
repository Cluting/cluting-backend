package com.cluting.clutingbackend.recruitingHome.repository;

import com.cluting.clutingbackend.plan.domain.RecruitSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitScheduleHomeRepository extends JpaRepository<RecruitSchedule, Long> {
    List<RecruitSchedule> findByPost_PostId(Long postId);

}
