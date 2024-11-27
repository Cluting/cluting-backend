package com.cluting.clutingbackend.docEval.repository;

import com.cluting.clutingbackend.plan.domain.Application;
import com.cluting.clutingbackend.plan.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Application2Repository extends JpaRepository<Application, Long> {
    List<Application> findByPostId(Long postId);
    Optional<Application> findByUserAndPostId(User user, Long postId);
    Optional<Application> findById(Long applicationId);
}