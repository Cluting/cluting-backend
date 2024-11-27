package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview,Long> {

}
