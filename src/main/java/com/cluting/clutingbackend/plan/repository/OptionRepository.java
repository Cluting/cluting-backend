package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByDocumentQuestionId(Long questionId);
}