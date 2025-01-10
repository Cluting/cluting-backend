package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.interview.domain.InterviewTimeSlot;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewTimeSlotRepository extends JpaRepository<InterviewTimeSlot, Long> {
    List<InterviewTimeSlot> findAllByRecruit_Id(Long recruitId);
    Optional<InterviewTimeSlot> findByTimeAndClubUser(LocalDateTime time, ClubUser clubUser);

    List<InterviewTimeSlot> findByRecruit(Recruit recruit);
}
