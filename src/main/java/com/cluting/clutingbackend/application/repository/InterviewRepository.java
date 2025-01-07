package com.cluting.clutingbackend.application.repository;

import com.cluting.clutingbackend.global.enums.EvaluateStatus;
import com.cluting.clutingbackend.interview.domain.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewRepository  extends JpaRepository<Interview,Long> {
    List<Interview> findByApplication_UserIdAndState(Long userId, EvaluateStatus state);

}
