package com.cluting.clutingbackend.application.repository;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.dto.response.RecruitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // [서류 평가하기] 해당 모집공고에 지원한 지원서 불러오기
    List<Application> findByRecruitId(Long recruitId);

    List<Application> findByUserId(Long userId);
    /////////////////////
    // 지원 중인 동아리

    // 지원한 동아리
    List<Application> findByUserIdAndRecruitStatus(Long userId, RecruitStatus recruitStatus);

//    // 스크랩한 동아리
//    List<Application> findByUserIdAndRecruit_Club_IdIn(Long userId, List<Long> clubIds);
//
//    // 최근 본 동아리
//    List<Application> findByUserIdOrderByCreatedAtDesc(Long userId);

}
