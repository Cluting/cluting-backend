package com.cluting.clutingbackend.evaluate.repository;

import com.cluting.clutingbackend.evaluate.domain.Criteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CriteriaRepository extends JpaRepository<Criteria, Long> {
}
