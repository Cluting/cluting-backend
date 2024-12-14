package com.cluting.clutingbackend.interview.repository;

import com.cluting.clutingbackend.interview.domain.Interview;
import com.cluting.clutingbackend.interview.domain.InterviewEvaluator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewEvaluatorRepository extends JpaRepository<InterviewEvaluator, Long> {
    List<InterviewEvaluator> findByInterviewIdIn(List<Long> interviewIds);
    int countDistinctByInterviewId(Long interviewId);
    List<InterviewEvaluator> findByInterview(Interview interview);
    Optional<InterviewEvaluator> findFirstByInterviewId(Long interviewId);

}
