package com.cluting.clutingbackend.evaluate.repository;

import com.cluting.clutingbackend.evaluate.domain.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}
