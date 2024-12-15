package com.cluting.clutingbackend.interview.repository;

import com.cluting.clutingbackend.interview.domain.InterviewScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewScoreRepository extends JpaRepository<InterviewScore,Long> {
    List<InterviewScore> findByInterviewEvaluatorId(Long evaluatorId);
}
