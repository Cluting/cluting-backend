package com.cluting.clutingbackend.docEval.repository;

import com.cluting.clutingbackend.plan.domain.Criteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Criteria2Repository extends JpaRepository<Criteria, Long> {
    Optional<Criteria> findByName(String name); // 기준 이름으로 Criteria를 조회
}