package com.cluting.clutingbackend.docEval.repository;

import com.cluting.clutingbackend.plan.domain.Application;
import com.cluting.clutingbackend.plan.domain.ClubUser;
import com.cluting.clutingbackend.evaluate.domain.Evaluation;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Evaluation2Repository extends JpaRepository<Evaluation, Long> {

    @Query("SELECT COUNT(DISTINCT e.clubUser.id) " +
            "FROM Evaluation e " +
            "WHERE e.application.id = :applicationId")
    Long countDistinctClubUsersByApplication(@Param("applicationId") Long applicationId);

    List<Evaluation> findByApplication_Id(Long applicationId); // 여러 개의 평가가 있을 수 있도록 수정

    Optional<Evaluation> findByApplicationAndClubUserAndStage(Application application, ClubUser clubUser, Evaluation.Stage stage);

    List<Evaluation> findByApplication_IdAndClubUser_Id(Long applicationId, Long clubUserId);
}
