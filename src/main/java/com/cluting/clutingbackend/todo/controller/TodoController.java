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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;


@Tag(name = "TODO", description = "리크루팅 TODO 관련 API")
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
    @Operation(
            summary = "TODO 생성",
            description = "사용자가 새로운 TODO 항목을 생성합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "TODO 생성 성공"),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 인증 토큰"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
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
    @Operation(
            summary = "TODO 수정",
            description = "특정 TODO 항목의 내용을 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "TODO 수정 성공"),
                    @ApiResponse(responseCode = "404", description = "TODO 항목을 찾을 수 없음"),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 인증 토큰")
            }
    )
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
    @Operation(
            summary = "TODO 상태 변경",
            description = "TODO의 완료/미완료 상태를 토글합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "TODO 상태 변경 성공"),
                    @ApiResponse(responseCode = "404", description = "TODO 항목을 찾을 수 없음"),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 인증 토큰")
            }
    )
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
    @Operation(
            summary = "TODO 예외 처리",
            description = "TODO를 찾을 수 없을 때 예외 응답을 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "404", description = "TODO 항목을 찾을 수 없음")
            }
    )
    @ExceptionHandler(TodoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleTodoNotFoundException(TodoNotFoundException ex) {
        return new ErrorResponse(ex.getErrorCode(), ex.getMessage());
    }
}
