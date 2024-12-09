package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.DocumentCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentCriteriaRepository extends JpaRepository<DocumentCriteria, Long> {
}
