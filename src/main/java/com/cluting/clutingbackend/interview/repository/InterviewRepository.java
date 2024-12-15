package com.cluting.clutingbackend.interview.repository;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.interview.domain.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByApplicationIdIn(List<Long> applicationIds);

    List<Interview> findByApplicationIn(List<Application> applications);

    Optional<Interview> findById(Long interviewId);

    List<Interview> findAllByApplicationIdIn(List<Long> applicationIds);

}
