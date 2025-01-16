package com.cluting.clutingbackend.interview.repository;

import com.cluting.clutingbackend.interview.domain.InterviewCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewCriteriaRepository extends JpaRepository<InterviewCriteria, Long> {
    List<InterviewCriteria> findByInterviewEvaluatorId(Long evaluatorId);

}
