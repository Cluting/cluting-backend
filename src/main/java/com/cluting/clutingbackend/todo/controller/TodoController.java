package com.cluting.clutingbackend.todo.controller;

import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.global.security.CustomUserDetailsService;
import com.cluting.clutingbackend.global.security.JwtProvider;
import com.cluting.clutingbackend.todo.dto.TodoRequest;
import com.cluting.clutingbackend.todo.dto.TodoResponse;
import com.cluting.clutingbackend.todo.service.TodoService;
import com.cluting.clutingbackend.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "[리크루팅 홈] 투두", description = "리크루팅 홈 - 투두 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todo")
public class TodoController {
    private final TodoService todoService;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    // [리크루팅 홈] 투두 작성하기
    @Operation(
            summary = "[리크루팅 홈] 투두 작성하기",
            description = "리크루팅 홈에서 투두를 작성합니다. 투두 내용과 상태(true면 완료, false면 미완료)를 전달 받습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "투두 작성 성공"),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 인증 토큰"),
                    @ApiResponse(responseCode = "404", description = "해당 투두 항목을 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(
            @RequestBody TodoRequest request,
            @RequestHeader("Authorization") String token
    ) {
        Long clubUserId = getClubUserIdFromToken(token);
        return ResponseEntity.ok(todoService.createTodo(clubUserId, request));
    }

    // 개인(운영진) 투두 삭제하기
    @Operation(
            summary = "[리크루팅 홈] 투두 삭제하기",
            description = "투두 ID와 유저 ID를 기반으로 리크루팅 홈에서 해당 투두를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "투두 삭제 성공"),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 인증 토큰"),
                    @ApiResponse(responseCode = "404", description = "해당 투두 항목을 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable Long todoId,
            @RequestHeader("Authorization") String token
    ) {
        Long clubUserId = getClubUserIdFromToken(token);
        todoService.deleteTodo(clubUserId, todoId);
        return ResponseEntity.noContent().build();
    }

    // [리크루팅 홈] 투두 완료 상태 바꾸기
    @Operation(
            summary = "[리크루팅 홈] 투두 완료 상태 바꾸기",
            description = "투두 ID와 유저 ID를 기반으로 해당 투두의 완료 상태(true:완료, false:미완료)를 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "투두 완료 상태 변경 성공"),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 인증 토큰"),
                    @ApiResponse(responseCode = "404", description = "해당 투두 항목을 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @PatchMapping("/status/{todoId}")
    public ResponseEntity<Void> toggleTodoStatus(
            @PathVariable Long todoId,
            @RequestHeader("Authorization") String token
    ) {
        Long clubUserId = getClubUserIdFromToken(token);
        todoService.toggleTodoStatus(clubUserId, todoId);
        return ResponseEntity.noContent().build();
    }

    // [리크루팅 홈] 투두 내용 변경하기
    @Operation(
            summary = "[리크루팅 홈] 투두 내용 변경하기",
            description = "투두 ID와 유저 ID, 투두 내용을 기반으로 해당 투두의 내용을 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "투두 내용 변경하기 성공"),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 인증 토큰"),
                    @ApiResponse(responseCode = "404", description = "해당 투두 항목을 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @PatchMapping("/{todoId}")
    public ResponseEntity<Void> updateTodoContent(
            @RequestHeader("Authorization") String token,
            @PathVariable Long todoId,
            @RequestBody TodoRequest request
    ) {
        Long clubUserId = getClubUserIdFromToken(token);
        todoService.updateTodoContent(clubUserId, todoId, request.getContent());
        return ResponseEntity.ok().build();
    }

    // [리크루팅 홈] 투두 리스트 완료/미완료 상태 분리해서 가져오기
    @Operation(
            summary = "[리크루팅 홈] 투두 완료 상태 바꾸기",
            description = "투두 ID와 유저 ID를 기반으로 해당 투두의 완료 상태(true:완료, false:미완료)를 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "투두 리스트 불러오기 성공"),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 인증 토큰"),
                    @ApiResponse(responseCode = "404", description = "해당 투두 항목을 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping
    public ResponseEntity<Map<String, List<TodoResponse>>> getTodoList(
            @RequestHeader("Authorization") String token
    ) {
        Long clubUserId = getClubUserIdFromToken(token);
        Map<String, List<TodoResponse>> todos = todoService.getTodosByStatus(clubUserId);
        return ResponseEntity.ok(todos);
    }

    public Long getClubUserIdFromToken(String token) {
        // 토큰에서 이메일 추출 -> 이메일로 User 조회 -> User의 ID 반환
        String email = jwtProvider.getUserEmail(token);
        User user = ((CustomUserDetails) customUserDetailsService.loadUserByUserId(email)).getUser();
        return user.getId();
    }

}
