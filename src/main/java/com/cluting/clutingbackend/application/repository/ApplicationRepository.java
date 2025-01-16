package com.cluting.clutingbackend.application.repository;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.dto.response.RecruitStatus;
import com.cluting.clutingbackend.global.enums.EvaluateStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // [서류 평가하기] 해당 모집공고에 지원한 지원서 불러오기
    List<Application> findByRecruitId(Long recruitId);
    List<Application> findAllByUserId(Long userId);
    Optional<Application> findByRecruitIdAndId(Long recruitId, Long applicationId);
    List<Application> findAllByRecruitId(Long recruitId);
    // [서류 평가하기] 해당 모집공고에 지원한 지원서 불러오기

    /////////////////////
    // 지원 중인 동아리

    // 지원한 동아리
    List<Application> findByUserIdAndRecruitStatus(Long userId, RecruitStatus recruitStatus);

    // 합격/붏합격한 동아리 조회
    List<Application> findByUserIdAndState(Long userId, EvaluateStatus state);

    // 지원한 동아리 가져오기
    Optional<Application> findByUserIdAndRecruitId(Long userId, Long recruitId);

}
