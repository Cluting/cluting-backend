package com.cluting.clutingbackend.interview.repository;

import com.cluting.clutingbackend.interview.domain.InterviewScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewScoreRepository extends JpaRepository<InterviewScore,Long> {
    List<InterviewScore> findByInterviewEvaluatorId(Long evaluatorId);

    Optional<InterviewScore> findByInterviewEvaluatorIdAndInterviewCriteriaId(Long interviewEvaluatorId, Long interviewCriteriaId);

}
