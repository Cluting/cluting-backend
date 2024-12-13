package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.DocumentEvalScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentEvalScoreRepository extends JpaRepository<DocumentEvalScore, Long> {
    List<DocumentEvalScore> findByDocumentEvaluatorId(Long evaluatorId);

    @Query("SELECT d FROM DocumentEvalScore d WHERE d.documentEvaluator.id = :evaluatorId AND d.documentEvaluator.clubUser.id = :clubUserId")
    List<DocumentEvalScore> findByEvaluatorIdAndClubUserId(@Param("evaluatorId") Long evaluatorId, @Param("clubUserId") Long clubUserId);
}
