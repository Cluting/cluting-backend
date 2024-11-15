package com.cluting.clutingbackend.todo.controller;

import com.cluting.clutingbackend.plan.domain.Todo;
import com.cluting.clutingbackend.plan.domain.User;
import com.cluting.clutingbackend.todo.exception.TodoNotFoundException;
import com.cluting.clutingbackend.todo.response.TodoResponse;
import com.cluting.clutingbackend.todo.service.TodoService;
import com.cluting.clutingbackend.todo.response.ErrorResponse;
import com.cluting.clutingbackend.util.security.CustomUserDetails;
import com.cluting.clutingbackend.util.security.CustomUserDetailsService;
import com.cluting.clutingbackend.util.security.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recruiting/todo")
public class TodoController {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final TodoService todoService;

    public TodoController(JwtProvider jwtProvider, CustomUserDetailsService customUserDetailsService, TodoService todoService) {
        this.jwtProvider = jwtProvider;
        this.customUserDetailsService = customUserDetailsService;
        this.todoService = todoService;
    }

    // Todo 생성
    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody Todo todo, @RequestHeader("Authorization") String token) {
        // 토큰에서 이메일 추출
        String email = jwtProvider.getUserEmail(token);

        // 이메일을 통해 User 객체 조회
        User user = ((CustomUserDetails) customUserDetailsService.loadUserByUserId(email)).getUser();

        // Todo 생성
        Todo createdTodo = todoService.createTodo(todo, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TodoResponse(createdTodo));
    }

    // Todo 수정
    @PatchMapping("/{todoId}")
    public ResponseEntity<TodoResponse> updateTodo(@PathVariable Long todoId, @RequestBody Todo todo, @RequestHeader("Authorization") String token) {
        // 토큰에서 이메일 추출
        String email = jwtProvider.getUserEmail(token);

        // 이메일을 통해 User 객체 조회
        User user = ((CustomUserDetails) customUserDetailsService.loadUserByUserId(email)).getUser();

        try {
            Todo updatedTodo = todoService.updateTodo(todoId, todo, user);
            return ResponseEntity.ok(new TodoResponse(updatedTodo)); // 수정된 Todo 반환
        } catch (TodoNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new TodoResponse(ex.getMessage())); // 예외 처리
        }
    }

    // Todo 상태 변경 (완료/미완료)
    @PatchMapping("/{todoId}/check")
    public ResponseEntity<TodoResponse> toggleTodoStatus(@PathVariable Long todoId, @RequestHeader("Authorization") String token) {

        // 토큰에서 이메일 추출
        String email = jwtProvider.getUserEmail(token);

        // 이메일을 통해 User 객체 조회
        User user = ((CustomUserDetails) customUserDetailsService.loadUserByUserId(email)).getUser();

        try {
            Todo updatedTodo = todoService.toggleTodoStatus(todoId, user);
            return ResponseEntity.ok(new TodoResponse(updatedTodo)); // 상태 변경 후 수정된 Todo 반환
        } catch (TodoNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new TodoResponse(ex.getMessage())); // 예외 처리
        }
    }


    // 예외 처리
    @ExceptionHandler(TodoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleTodoNotFoundException(TodoNotFoundException ex) {
        return new ErrorResponse(ex.getErrorCode(), ex.getMessage());
    }
}
