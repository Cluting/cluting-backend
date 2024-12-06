package com.cluting.clutingbackend.recruit.controller;

import com.cluting.clutingbackend.global.enums.Category;
import com.cluting.clutingbackend.global.enums.ClubType;
import com.cluting.clutingbackend.global.enums.SortType;
import com.cluting.clutingbackend.recruit.dto.response.RecruitResponseDto;
import com.cluting.clutingbackend.recruit.dto.response.RecruitsResponseDto;
import com.cluting.clutingbackend.recruit.service.RecruitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RecruitController {
    private final RecruitService recruitService;

    @Operation(description = "홈화면 동아리 리스트 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "동아리 조회 성공"),
            @ApiResponse(responseCode = "404", description = "동아리 조회 실패"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/list")
    @ResponseStatus(value = HttpStatus.OK)
    public RecruitsResponseDto findPosts(
            @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
            @RequestParam(value = "sortType", required = false) SortType sortType, // 마감임박순, 최신순, 오래된 순
            @RequestParam(value = "clubType", required = false) ClubType clubType, // 연합동아리, 교내동아리
            @RequestParam(value = "fieldType", required = false) Category category) { // 동아리 분류
        return recruitService.findAll(pageNum, sortType, clubType, category);
    }

    @Operation(description = "ID로 리크루팅 단일 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리크루팅 조회 성공"),
            @ApiResponse(responseCode = "404", description = "리크루팅 조회 실패"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/{recruitId}")
    @ResponseStatus(value = HttpStatus.OK)
    public RecruitResponseDto findPosts(
            @PathVariable("recruitId") Long recruitId) {
        return recruitService.findById(recruitId);
    }
}
