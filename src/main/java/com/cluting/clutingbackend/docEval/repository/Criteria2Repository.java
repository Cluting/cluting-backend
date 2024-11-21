package com.cluting.clutingbackend.docEval.repository;

import com.cluting.clutingbackend.plan.domain.Criteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Criteria2Repository extends JpaRepository<Criteria, Long> {}