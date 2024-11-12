package com.cluting.clutingbackend.recruitingHome.todo.controller;

import com.cluting.clutingbackend.recruitingHome.todo.domain.Todo;
import com.cluting.clutingbackend.recruitingHome.todo.domain.User;
import com.cluting.clutingbackend.recruitingHome.todo.exception.TodoNotFoundException;
import com.cluting.clutingbackend.recruitingHome.todo.response.TodoResponse;
import com.cluting.clutingbackend.recruitingHome.todo.service.TodoService;
import com.cluting.clutingbackend.recruitingHome.todo.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recruiting/todo")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // Todo 생성
    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody Todo todo, @RequestHeader("Authorization") String token) {
//        // 토큰에서 userId를 추출하여 User 객체를 조회
//        Long userId = jwtTokenProvider.getUserIdFromToken(token);  // JWT 토큰에서 userId 추출 (실제 구현에 맞게 변경)
//        User user = userService.getUserById(userId);  // userId를 통해 실제 User 객체 조회

        //아래는 임시 코드
        User user = new User();
        user.setUserId(1L);  // 임시로 User 객체의 ID를 1로 설정

        // Todo 생성
        Todo createdTodo = todoService.createTodo(todo, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TodoResponse(createdTodo));
    }

    // Todo 수정
    @PatchMapping("/{todoId}")
    public ResponseEntity<TodoResponse> updateTodo(@PathVariable Long todoId, @RequestBody Todo todo, @RequestHeader("Authorization") String token) {
//        // 토큰에서 userId를 추출하여 User 객체를 조회
//        Long userId = jwtTokenProvider.getUserIdFromToken(token);  // JWT 토큰에서 userId 추출
//        User user = userService.getUserById(userId);  // userId를 통해 실제 User 객체 조회

        //아래는 임시 코드
        User user = new User();
        user.setUserId(1L);  // 임시로 User 객체의 ID를 1로 설정

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
//        // 토큰에서 userId를 추출하여 User 객체를 조회
//        Long userId = jwtTokenProvider.getUserIdFromToken(token);  // JWT 토큰에서 userId 추출
//        User user = userService.getUserById(userId);  // userId를 통해 실제 User 객체 조회

        //아래는 임시 코드
        User user = new User();
        user.setUserId(1L);  // 임시로 User 객체의 ID를 1로 설정

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
