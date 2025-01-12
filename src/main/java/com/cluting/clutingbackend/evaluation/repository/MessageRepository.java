package com.cluting.clutingbackend.evaluation.repository;

import com.cluting.clutingbackend.evaluation.domain.Message;
import com.cluting.clutingbackend.global.enums.EvalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findByRecruit_IdAndAndEvalType(Long recruitId, EvalType evalType);
}
