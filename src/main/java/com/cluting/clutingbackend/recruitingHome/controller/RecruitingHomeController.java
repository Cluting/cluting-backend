package com.cluting.clutingbackend.recruitingHome.controller;

import com.cluting.clutingbackend.plan.domain.*;
import com.cluting.clutingbackend.recruitingHome.response.RecruitingHomeResponse;
import com.cluting.clutingbackend.recruitingHome.service.RecruitingHomeService;
import com.cluting.clutingbackend.util.security.CustomUserDetails;
import com.cluting.clutingbackend.util.security.CustomUserDetailsService;
import com.cluting.clutingbackend.util.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/recruiting")
public class RecruitingHomeController {

    private final RecruitingHomeService recruitingHomeService;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public RecruitingHomeController(RecruitingHomeService recruitingHomeService,
                                    JwtProvider jwtProvider,
                                    CustomUserDetailsService customUserDetailsService) {
        this.recruitingHomeService = recruitingHomeService;
        this.jwtProvider = jwtProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping
    public ResponseEntity<?> getRecruitingData(
            @RequestParam Long clubId,
            @RequestParam Long postId,
            @RequestHeader("Authorization") String token) {

        // 토큰에서 이메일 추출
        String email = jwtProvider.getUserEmail(token);

        // 이메일을 통해 User 객체 조회
        User user = ((CustomUserDetails) customUserDetailsService.loadUserByUserId(email)).getUser();

        // 2. Club, Post, RecruitSchedule, ClubUser, Todo 데이터를 서비스로부터 가져오기
        Optional<Club> club = recruitingHomeService.getClubById(clubId);
        Optional<Post> post = recruitingHomeService.getPostByClubId(clubId, postId);
        List<RecruitSchedule> recruitSchedules = recruitingHomeService.getRecruitSchedulesByPostId(postId);
        List<ClubUser> clubUsers = recruitingHomeService.getClubUsersByClubId(clubId);
        List<Todo> todos = recruitingHomeService.getTodosByUserId(user.getId());

        if (club.isEmpty() || post.isEmpty()) {
            return ResponseEntity.badRequest().body("Club or Post not found.");
        }
        System.out.println("Club ID: " + clubId);
        System.out.println("Post ID: " + postId);
        System.out.println("Authorization Token: " + token);

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
}
