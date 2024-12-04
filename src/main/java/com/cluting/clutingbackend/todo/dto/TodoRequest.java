package com.cluting.clutingbackend.todo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// [리크루팅 홈] 투두 작성 시 DTO
public class TodoRequest {
    private String content;  //투두 내용
}
