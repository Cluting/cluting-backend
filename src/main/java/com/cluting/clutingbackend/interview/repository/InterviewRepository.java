package com.cluting.clutingbackend.interview.repository;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.global.enums.EvaluateStatus;
import com.cluting.clutingbackend.interview.domain.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByApplicationIdIn(List<Long> applicationIds);

    List<Interview> findByApplicationIn(List<Application> applications);

    Optional<Interview> findById(Long interviewId);

    List<Interview> findAllByApplicationIdIn(List<Long> applicationIds);

    List<Interview> findAllByApplication_Recruit_Id(Long recruitId);

    @Query("SELECT i FROM Interview i WHERE i.application.user.id = :userId AND i.application.recruit.id = :recruitId")
    Optional<Interview> findByUser_IdAndRecruit_Id(@Param("userId") Long userId,
                                                   @Param("recruitId") Long recruitId);

    List<Interview> findByApplication_UserIdAndState(Long userId, EvaluateStatus state);


}
