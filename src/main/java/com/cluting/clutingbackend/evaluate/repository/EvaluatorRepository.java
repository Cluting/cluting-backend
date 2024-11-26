package com.cluting.clutingbackend.evaluate.repository;

import com.cluting.clutingbackend.evaluate.domain.Evaluator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluatorRepository extends JpaRepository<Evaluator, Long> {
}
