package com.cluting.clutingbackend.application.repository;

import com.cluting.clutingbackend.application.domain.ApplicantInterviewTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationInterviewTimeSlotRepository extends JpaRepository<ApplicantInterviewTimeSlot, Long> {
}
