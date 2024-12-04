package com.cluting.clutingbackend.todo.repository;

import com.cluting.clutingbackend.todo.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    // [리크루팅 홈] 투두 리스트 가져오기
    @Query("SELECT t FROM Todo t WHERE t.user.id = :userId")
    List<Todo> findTodosByUserId(Long userId);
}
