package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByPost_Id(Long postId);

    @Query("SELECT a FROM Application a WHERE a.applicationId = :id AND a.state = :state")
    List<Application> findByState(
            @Param("id") Long id,
            @Param("state") Application.State state);
}
