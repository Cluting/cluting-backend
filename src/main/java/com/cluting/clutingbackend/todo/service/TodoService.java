package com.cluting.clutingbackend.todo.service;

import com.cluting.clutingbackend.todo.exception.TodoNotFoundException;
import com.cluting.clutingbackend.todo.repository.TodoRepository;
import com.cluting.clutingbackend.plan.domain.User;
import com.cluting.clutingbackend.plan.domain.Todo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // Todo 작성하기 (User 객체를 기준으로 작성)
    public Todo createTodo(Todo todo, User user) {
        todo.setUser(user);  // User 객체를 Todo에 설정
        return todoRepository.save(todo);
    }

    // Todo 수정하기 (User 객체를 기준으로 수정)
    public Todo updateTodo(Long todoId, Todo todoDetails, User user) {
        return todoRepository.findById(todoId)
                .filter(todo -> todo.getUser().getId().equals(user.getId())) // userId만 비교
                .map(todo -> {
                    todo.setContent(todoDetails.getContent()); // Todo 내용 업데이트
                    todo.setStatus(todoDetails.getStatus()); // Todo 완료 상태 업데이트
                    return todoRepository.save(todo);
                })
                .orElseThrow(() -> TodoNotFoundException.withId(todoId));
    }


    // Todo 완료 상태 바꾸기 (User 객체를 기준으로 상태 변경)
    public Todo toggleTodoStatus(Long todoId, User user) {
        Todo todo = todoRepository.findById(todoId)
                .filter(t -> t.getUser().getId().equals(user.getId())) // userId만 비교
                .orElseThrow(() -> TodoNotFoundException.withId(todoId)); // Todo를 찾지 못한 경우 예외 처리
        todo.setStatus(!todo.getStatus());  // 상태 반전
        return todoRepository.save(todo);
    }

    // Todo 목록 가져오기 (User 객체를 기준으로)
    public List<Todo> getTodosByUser(User user) {
        return todoRepository.findByUserId(user.getId());
    }
}
