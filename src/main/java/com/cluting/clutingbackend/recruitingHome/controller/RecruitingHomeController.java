package com.cluting.clutingbackend.recruitingHome.controller;

import com.cluting.clutingbackend.plan.domain.*;
import com.cluting.clutingbackend.recruitingHome.response.RecruitingHomeResponse;
import com.cluting.clutingbackend.recruitingHome.service.RecruitingHomeService;
import com.cluting.clutingbackend.util.security.CustomUserDetails;
import com.cluting.clutingbackend.util.security.CustomUserDetailsService;
import com.cluting.clutingbackend.util.security.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Tag(name = "리크루팅 홈", description = "리크루팅 홈 관련 API (시작 후)")
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


    @Operation(
            summary = "리크루팅 홈 데이터 조회",
            description = "클럽 ID와 모집 공고 ID를 기반으로 리크루팅 홈 데이터를 반환합니다. 반환 데이터에는 동아리 정보, 모집 공고 정보, 리크루팅 일정, 운영진 목록, TODO 목록이 포함됩니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "리크루팅 홈 데이터 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "클럽 또는 모집 공고를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
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
