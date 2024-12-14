package com.cluting.clutingbackend.interview.repository;

import com.cluting.clutingbackend.interview.domain.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByApplicationIdIn(List<Long> applicationIds);
}
