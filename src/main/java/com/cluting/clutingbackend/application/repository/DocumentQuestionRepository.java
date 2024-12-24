package com.cluting.clutingbackend.application.repository;

import com.cluting.clutingbackend.plan.domain.DocumentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentQuestionRepository extends JpaRepository<DocumentQuestion,Long> {
}
