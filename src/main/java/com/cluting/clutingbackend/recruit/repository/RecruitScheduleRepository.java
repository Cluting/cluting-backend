package com.cluting.clutingbackend.recruit.repository;

import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.recruit.domain.RecruitSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruitScheduleRepository extends JpaRepository<RecruitSchedule, Long> {

    // [리크루팅 홈] 리크루팅 일정 가져오기
    @Query("SELECT rs FROM RecruitSchedule rs WHERE rs.recruit.id = :recruitId")
    RecruitSchedule findScheduleByRecruitId(Long recruitId);
    // [계획하기] 불러오기
    Optional<RecruitSchedule> findByRecruitId(Long recruitId);

}
