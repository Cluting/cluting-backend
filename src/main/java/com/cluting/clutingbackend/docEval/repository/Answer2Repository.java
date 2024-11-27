package com.cluting.clutingbackend.docEval.repository;

import com.cluting.clutingbackend.plan.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Answer2Repository extends JpaRepository<Answer, Long> {
    List<Answer> findByApplication_Id(Long applicationId);
}