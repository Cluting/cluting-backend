package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot,Long> {
    List<TimeSlot> findByInterviewId(Long interviewId);
}
