package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.DocumentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentAnswerRepository extends JpaRepository<DocumentAnswer, Long> {
    List<DocumentAnswer> findByApplicationId(Long applicationId);
}
