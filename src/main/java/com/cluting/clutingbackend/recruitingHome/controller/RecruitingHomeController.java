package com.cluting.clutingbackend.recruitingHome.controller;

import com.cluting.clutingbackend.plan.domain.*;
import com.cluting.clutingbackend.recruitingHome.response.RecruitingHomeResponse;
import com.cluting.clutingbackend.recruitingHome.service.RecruitingHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/recruiting")
public class RecruitingHomeController {

    private final RecruitingHomeService recruitingHomeService;

    @Autowired
    public RecruitingHomeController(RecruitingHomeService recruitingHomeService) {
        this.recruitingHomeService = recruitingHomeService;
    }

    @GetMapping
    public ResponseEntity<?> getRecruitingData(
            @RequestParam Long clubId,
            @RequestParam Long postId,
            @RequestHeader("Authorization") String authorizationToken) {

        // 1. 사용자 정보 가져오기 (토큰에서 userId 추출)
        Long userId = extractUserIdFromToken(authorizationToken);  // 예시: 토큰에서 사용자 정보를 추출하는 메서드

        // 2. Club, Post, RecruitSchedule, ClubUser, Todo 데이터를 서비스로부터 가져오기
        Optional<Club> club = recruitingHomeService.getClubById(clubId);
        Optional<Post> post = recruitingHomeService.getPostByClubId(clubId, postId);
        List<RecruitSchedule> recruitSchedules = recruitingHomeService.getRecruitSchedulesByPostId(postId);
        List<ClubUser> clubUsers = recruitingHomeService.getClubUsersByClubId(clubId);
        List<Todo> todos = recruitingHomeService.getTodosByUserId(userId);

        if (club.isEmpty() || post.isEmpty()) {
            return ResponseEntity.badRequest().body("Club or Post not found.");
        }
        System.out.println("Club ID: " + clubId);
        System.out.println("Post ID: " + postId);
        System.out.println("Authorization Token: " + authorizationToken);

        // 응답 데이터 포맷팅
        RecruitingHomeResponse response = new RecruitingHomeResponse(
                club.get().getName(),
                post.get().getCurrentStage(),
                post.get().getIsInterview(),
                post.get().getGeneration(),
                recruitSchedules,
                clubUsers,
                todos
        );

        return ResponseEntity.ok(response);
    }


    // 예시로 Authorization 토큰에서 userId를 추출하는 방법
    private Long extractUserIdFromToken(String token) {
        // 실제 구현에서는 JWT 토큰을 파싱하거나, 서비스에서 User 정보를 조회하는 방식으로 구현
        // 예: JWT 토큰을 파싱하여 userId를 가져오기
        return 1L; // 예시로 userId가 1L인 경우 반환
    }
}
