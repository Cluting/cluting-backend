package com.cluting.clutingbackend.docEval.repository;

import com.cluting.clutingbackend.plan.domain.Evaluation;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Evaluation2Repository extends JpaRepository<Evaluation, Long> {
    @Query("SELECT COUNT(DISTINCT e.clubUser.clubUserId) " +
            "FROM Evaluation e " +
            "WHERE e.application.applicationId = :applicationId")
    Long countDistinctClubUsersByApplication(@Param("applicationId") Long applicationId);
    List<Evaluation> findByApplication_ApplicationId(Long applicationId);
    List<Evaluation> findByClubUser_UserIdAndApplication_ApplicationId(Long clubUserId, Long applicationId);

}
