package com.cluting.clutingbackend.recruit.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
// [리크루팅 홈] 개인 투두 리스트 GET
public class TodoDto {
    private Long todoId;
    private String content;
    private Boolean status;
}
