package com.cluting.clutingbackend.application.repository;

import com.cluting.clutingbackend.application.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByRecruit_Id(Long recruitId);
}
