package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.application.domain.ApplicantInterviewTimeSlot;
import com.cluting.clutingbackend.interview.domain.InterviewTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewTimeSlotRepository extends JpaRepository<InterviewTimeSlot, Long> {
    List<InterviewTimeSlot> findAllByRecruit_Id(Long recruitId);

    @Query("SELECT i FROM InterviewTimeSlot i " +
            "WHERE i.time = :time AND i.clubUser.id = :clubUserId")
    Optional<InterviewTimeSlot> findByTimeAndClubUserId(@Param("time") LocalDateTime time, @Param("clubUserId") Long clubUserId);
}
