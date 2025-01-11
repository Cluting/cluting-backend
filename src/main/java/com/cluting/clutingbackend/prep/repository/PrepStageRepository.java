package com.cluting.clutingbackend.prep.repository;

import com.cluting.clutingbackend.prep.domain.PrepStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PrepStageRepository extends JpaRepository<PrepStage, Long> {
    // [계획하기] 불러오기
    List<PrepStage> findByRecruitId(Long recruitId);

    Optional<PrepStage> findByRecruitIdAndStageOrder(Long recruitId, Integer stageOrder);

}