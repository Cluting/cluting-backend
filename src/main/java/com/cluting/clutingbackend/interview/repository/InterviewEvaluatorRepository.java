package com.cluting.clutingbackend.interview.repository;

import com.cluting.clutingbackend.interview.domain.InterviewEvaluator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewEvaluatorRepository extends JpaRepository<InterviewEvaluator, Long> {
    List<InterviewEvaluator> findByInterviewIdIn(List<Long> interviewIds);
    int countDistinctByInterviewId(Long interviewId);
}
