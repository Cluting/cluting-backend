package com.cluting.clutingbackend.docEval.repository;

import com.cluting.clutingbackend.plan.domain.Application;
import com.cluting.clutingbackend.plan.domain.ClubUser;
import com.cluting.clutingbackend.plan.domain.Evaluation;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Evaluation2Repository extends JpaRepository<Evaluation, Long> {

    @Query("SELECT COUNT(DISTINCT e.clubUser.clubUserId) " +
            "FROM Evaluation e " +
            "WHERE e.application.applicationId = :applicationId")
    Long countDistinctClubUsersByApplication(@Param("applicationId") Long applicationId);

    List<Evaluation> findByApplication_ApplicationId(Long applicationId); // 여러 개의 평가가 있을 수 있도록 수정

    List<Evaluation> findByClubUser_UserIdAndApplication_ApplicationId(Long clubUserId, Long applicationId);

    Optional<Evaluation> findByApplicationAndClubUserAndStage(Application application, ClubUser clubUser, Evaluation.Stage stage);

    // 여러 개의 결과가 있을 수 있으므로 Optional에서 List로 수정
    List<Evaluation> findByApplication_ApplicationIdAndClubUser_ClubUserId(Long applicationId, Long clubUserId); // 수정
}
