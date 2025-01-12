package com.cluting.clutingbackend.prep.repository;

import com.cluting.clutingbackend.prep.domain.PrepStage;
import com.cluting.clutingbackend.prep.domain.PrepStageClubUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrepStageClubUserRepository extends JpaRepository<PrepStageClubUser, Long> {
    // [계획하기] 불러오기
    List<PrepStageClubUser> findByPrepStageId(Long prepStageId);
    List<PrepStageClubUser> findAllByPrepStageIn(List<PrepStage> prepStages);

    @Modifying
    @Query("DELETE FROM PrepStageClubUser p WHERE p.prepStage.id = :prepStageId")
    void deleteAllByPrepStageId(@Param("prepStageId") Long prepStageId);

    @Query("SELECT p FROM PrepStageClubUser p WHERE p.prepStage.id = :prepStageId")
    List<PrepStageClubUser> findAllByPrepStageId(@Param("prepStageId") Long prepStageId);
}