package com.cluting.clutingbackend.application.repository;

import com.cluting.clutingbackend.application.domain.ApplicantInterviewTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicantInterviewTimeSlotRepository extends JpaRepository<ApplicantInterviewTimeSlot, Long> {
    List<ApplicantInterviewTimeSlot> findAllByApplication_Recruit_Id(Long recruitId);

    List<ApplicantInterviewTimeSlot> findByApplication_Id(Long applicationId);

    ApplicantInterviewTimeSlot findByApplication_IdAndIsAssignedTrue(Long applicationId);
}
