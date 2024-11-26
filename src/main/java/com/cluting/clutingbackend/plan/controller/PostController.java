package com.cluting.clutingbackend.plan.controller;

import com.cluting.clutingbackend.plan.domain.Club;
import com.cluting.clutingbackend.plan.dto.response.PostsResponseDto;
import com.cluting.clutingbackend.plan.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;

    @Operation(description = "홈화면 동아리 리스트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "동아리 조회 성공"),
            @ApiResponse(responseCode = "404", description = "동아리 조회 실패"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/list")
    @ResponseStatus(value = HttpStatus.OK)
    public PostsResponseDto findPosts(
            @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
            @RequestParam(value = "sortType", required = false) String sortType,
            @RequestParam(value = "clubType", required = false) Club.Type clubType,
            @RequestParam(value = "fieldType", required = false) Club.Category fieldType) {
        return postService.getPosts(pageNum, sortType, clubType, fieldType);
    }
}
