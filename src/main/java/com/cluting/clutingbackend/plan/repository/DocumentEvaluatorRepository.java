package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.DocumentEvaluator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentEvaluatorRepository extends JpaRepository<DocumentEvaluator, Long> {
    // [서류 평가하기] 평가 전 리스트 불러오기
    @Query("SELECT de FROM DocumentEvaluator de WHERE de.application.id IN :applicationIds AND de.stage = 'BEFORE'")
    List<DocumentEvaluator> findByApplicationIdsAndStageBefore(@Param("applicationIds") List<Long> applicationIds);

    // [서류 평가하기] 지원서 ID로 DocumentEvaluator 찾기
    DocumentEvaluator findByApplicationId(Long applicationId);

    // [서류 평가하기] 평가할 전체 운영진 수
    @Query("SELECT COUNT(DISTINCT de.clubUser.id) FROM DocumentEvaluator de " +
            "JOIN de.application a WHERE a.recruit.id = :recruitId " +
            "AND a.id = :applicationId")
    int countUniqueClubUserIdsByRecruitIdAndApplicationId(Long recruitId, Long applicationId);

}