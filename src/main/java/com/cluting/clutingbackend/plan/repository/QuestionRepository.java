package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question,Long> {
}
