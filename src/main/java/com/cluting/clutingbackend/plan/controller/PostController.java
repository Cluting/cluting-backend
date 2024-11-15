package com.cluting.clutingbackend.plan.controller;

import com.cluting.clutingbackend.plan.domain.Club;
import com.cluting.clutingbackend.plan.dto.response.PostsResponseDto;
import com.cluting.clutingbackend.plan.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;

    // 동아리 공고 리스트
    @GetMapping("/recruiting")
    @ResponseStatus(value = HttpStatus.OK)
    public PostsResponseDto findPosts(
            @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
            @RequestParam(value = "sortType", required = false) String sortType,
            @RequestParam(value = "clubType", required = false) Club.Type clubType,
            @RequestParam(value = "fieldType", required = false) Club.Category fieldType) {
        return postService.getPosts(pageNum, sortType, clubType, fieldType);
    }
}
