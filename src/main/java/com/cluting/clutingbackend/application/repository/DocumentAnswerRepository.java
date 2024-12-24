package com.cluting.clutingbackend.application.repository;

import com.cluting.clutingbackend.plan.domain.DocumentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentAnswerRepository extends JpaRepository<DocumentAnswer,Long>{

    List<DocumentAnswer> findByApplicationId(Long applicationId);

}
