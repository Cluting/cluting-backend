package com.cluting.clutingbackend.plan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PostsResponseDto {
    private Long count;
    private List<PostResponseDto> posts;
}
