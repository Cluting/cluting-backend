package com.cluting.clutingbackend.application.repository;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // [서류 평가하기] 해당 모집공고에 지원한 지원서 불러오기
    List<Application> findByRecruitId(Long recruitId);

    Optional<Application> findByRecruitIdAndId(Long recruitId, Long applicationId);

    List<Application> findAllByRecruitId(Long recruitId);

}
