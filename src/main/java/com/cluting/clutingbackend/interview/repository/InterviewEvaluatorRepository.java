package com.cluting.clutingbackend.interview.repository;

import com.cluting.clutingbackend.interview.domain.Interview;
import com.cluting.clutingbackend.interview.domain.InterviewEvaluator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewEvaluatorRepository extends JpaRepository<InterviewEvaluator, Long> {
    List<InterviewEvaluator> findByInterviewIdIn(List<Long> interviewIds);
    int countDistinctByInterviewId(Long interviewId);
    List<InterviewEvaluator> findByInterview(Interview interview);
    Optional<InterviewEvaluator> findFirstByInterviewId(Long interviewId);
    List<InterviewEvaluator> findByInterviewId(Long interviewId);
    Optional<InterviewEvaluator> findByInterviewIdAndClubUserId(Long interviewId, Long clubUserId);
    List<InterviewEvaluator> findAllByInterviewIdIn(List<Long> interviewIds);

}
