package com.cluting.clutingbackend.todo.exception;

import lombok.Getter;

// Todo에서 발생할 수 있는 예외 클래스
@Getter
public class TodoNotFoundException extends RuntimeException {
    private final String errorCode;

    public TodoNotFoundException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    //아이디로 Todo를 찾지 못한 경우
    public static TodoNotFoundException withId(Long todoId) {
        return new TodoNotFoundException("Todo Id 없음. Todo Id: " + todoId, "TODO_NOT_FOUND");
    }
}
