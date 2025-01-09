package com.cluting.clutingbackend.application.repository;

import com.cluting.clutingbackend.application.domain.ApplicantInterviewTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicantInterviewTimeSlotRepository extends JpaRepository<ApplicantInterviewTimeSlot, Long> {
    List<ApplicantInterviewTimeSlot> findAllByApplication_Recruit_Id(Long recruitId);

    List<ApplicantInterviewTimeSlot> findByApplication_Id(Long applicationId);

    ApplicantInterviewTimeSlot findByApplication_IdAndIsAssignedTrue(Long applicationId);

    @Query("SELECT a FROM ApplicantInterviewTimeSlot a " +
            "WHERE a.time = :time AND a.application.user.id = :userId")
    Optional<ApplicantInterviewTimeSlot> findByTimeAndUserId(@Param("time") LocalDateTime time, @Param("userId") Long userId);
}
