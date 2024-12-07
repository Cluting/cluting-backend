package com.cluting.clutingbackend.todo.dto;

import com.cluting.clutingbackend.todo.domain.Todo;
import lombok.Getter;

@Getter
// [리크루팅 홈] 투두 Response DTO
public class TodoResponse {
    private Long id;  // 투두 아이디
    private String content;  // 투두 내용
    private Boolean status; // 투두 상태(true:완료, false:미완료)

    public static TodoResponse fromEntity(Todo todo) {
        TodoResponse response = new TodoResponse();
        response.id = todo.getId();
        response.content = todo.getContent();
        response.status = todo.getStatus();
        return response;
    }
}
