package com.cluting.clutingbackend.interview.repository;

import com.cluting.clutingbackend.interview.domain.InterviewAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InterviewAnswerRepository extends JpaRepository<InterviewAnswer, Long> {
    Optional<InterviewAnswer> findByInterviewQuestionId(Long questionId);
}