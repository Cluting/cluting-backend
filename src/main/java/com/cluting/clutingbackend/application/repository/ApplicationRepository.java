package com.cluting.clutingbackend.application.repository;

import com.cluting.clutingbackend.application.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // [서류 평가하기] 해당 모집공고에 지원한 지원서 불러오기
    List<Application> findByRecruitId(Long recruitId);

}
