package com.cluting.clutingbackend.docEval.repository;

import com.cluting.clutingbackend.plan.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Application2Repository extends JpaRepository<Application, Long> {
    List<Application> findByPostId(Long postId);
}