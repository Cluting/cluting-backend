package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.ClubUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubUserRepository extends JpaRepository<ClubUser, Long> {

    @Query("SELECT cs FROM ClubUser cs WHERE cs.club.id = :clubId AND cs.role = 'STAFF'")
    List<ClubUser> clubStaff(@Param("clubId") Long clubId);
}
