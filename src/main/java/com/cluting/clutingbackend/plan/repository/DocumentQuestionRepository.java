package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.DocumentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentQuestionRepository extends JpaRepository<DocumentQuestion, Long> {
    List<DocumentQuestion> findByGroupId(Long groupId);
}

