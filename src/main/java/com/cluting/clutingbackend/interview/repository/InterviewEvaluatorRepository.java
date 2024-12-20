package com.cluting.clutingbackend.interview.repository;

import com.cluting.clutingbackend.interview.domain.InterviewEvaluator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewEvaluatorRepository extends JpaRepository<InterviewEvaluator, Long> {
    InterviewEvaluator findByInterviewId(Long interviewId);

    InterviewEvaluator findByGroupId(Long groupId);
}
