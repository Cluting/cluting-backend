package com.cluting.clutingbackend.recruit.repository;

import com.cluting.clutingbackend.recruit.domain.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Long> {
    // [리크루팅 홈] 모집공고 정보 가져오기
    @Query("SELECT r FROM Recruit r WHERE r.id = :recruitId")
    Recruit findRecruitById(Long recruitId);
}
