package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimeSlotRepository extends JpaRepository<TimeSlot,Long> {
    List<TimeSlot> findByInterviewId(Long interviewId);

    Optional<TimeSlot> findByInterviewIdAndTimeSlot(Long interviewId, LocalDateTime time);
}
