package com.cluting.clutingbackend.recruitingHome.repository;

import com.cluting.clutingbackend.plan.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRecruitingHomeRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUser_UserId(Long userId);

}
