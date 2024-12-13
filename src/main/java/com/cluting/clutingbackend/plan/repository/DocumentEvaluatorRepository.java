package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.plan.domain.DocumentEvaluator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocumentEvaluatorRepository extends JpaRepository<DocumentEvaluator, Long> {
    // [서류 평가하기] 평가 전 리스트 불러오기
    @Query("SELECT de FROM DocumentEvaluator de WHERE de.application.id IN :applicationIds AND de.stage = 'BEFORE'")
    List<DocumentEvaluator> findByApplicationIdsAndStageBefore(@Param("applicationIds") List<Long> applicationIds);

    // [서류 평가하기] 지원서 ID로 DocumentEvaluator 찾기
    List<DocumentEvaluator> findByApplicationId(Long applicationId);

    // [서류 평가하기] 평가할 전체 운영진 수
    @Query("SELECT COUNT(DISTINCT de.clubUser.id) FROM DocumentEvaluator de " +
            "JOIN de.application a WHERE a.recruit.id = :recruitId " +
            "AND a.id = :applicationId")
    int countUniqueClubUserIdsByRecruitIdAndApplicationId(Long recruitId, Long applicationId);

    // [서류 평가하기] 담당 지원서 찾기
    List<DocumentEvaluator> findByClubUser(ClubUser clubUser);

    // [서류 평가하기] Recruit ID를 기반으로 DocumentEvaluator 조회
    @Query("SELECT de FROM DocumentEvaluator de " +
            "JOIN de.application app " +
            "WHERE app.recruit.id = :recruitId")
    List<DocumentEvaluator> findByRecruitId(@Param("recruitId") Long recruitId);

    // [서류 평가하기] Group ID로 DocumentEvaluator 조회
    List<DocumentEvaluator> findByGroupId(Long groupId);

    List<DocumentEvaluator> findAllByApplicationId(Long applicationId);

}

