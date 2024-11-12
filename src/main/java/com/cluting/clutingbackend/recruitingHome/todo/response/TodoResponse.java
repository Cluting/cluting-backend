package com.cluting.clutingbackend.recruitingHome.todo.response;

import com.cluting.clutingbackend.plan.domain.Todo;
import lombok.Getter;

@Getter
public class TodoResponse {
    private Todo todo;
    private String errorMessage;

    // 정상적인 경우 Todo 객체를 받는 생성자
    public TodoResponse(Todo todo) {
        this.todo = todo;
    }

    // 예외 처리 시 String 메시지를 받는 생성자
    public TodoResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
