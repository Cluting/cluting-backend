package com.cluting.clutingbackend.recruitingHome.todo.repository;

import com.cluting.clutingbackend.plan.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserUserId(Long userId);
}
