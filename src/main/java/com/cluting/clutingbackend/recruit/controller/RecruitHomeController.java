package com.cluting.clutingbackend.recruit.controller;


import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.global.security.CustomUserDetailsService;
import com.cluting.clutingbackend.global.security.JwtProvider;
import com.cluting.clutingbackend.recruit.dto.RecruitHomeDto;
import com.cluting.clutingbackend.recruit.service.RecruitHomeService;
import com.cluting.clutingbackend.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[리크루팅 홈]", description = "리크루팅 홈 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recruiting/home")
public class RecruitHomeController {
    private final RecruitHomeService recruitService;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    // [리크루팅 홈] 불러오기
    @Operation(
            summary = "[리크루팅 홈] 불러오기",
            description = "동아리 ID와 모집 공고 ID를 기반으로 리크루팅 홈 데이터를 반환합니다. 반환 데이터에는 동아리 정보, 모집 공고 정보, 리크루팅 일정, 운영진 목록, TODO 목록이 포함됩니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "리크루팅 홈 데이터 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "클럽 또는 모집 공고를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping
    public RecruitHomeDto getRecruitHome(
            @RequestParam Long recruitId,
            @RequestParam Long clubId,
            @RequestHeader("Authorization") String token) {
        // 토큰에서 이메일 추출
        String email = jwtProvider.getUserEmail(token);

        // 이메일을 통해 User 객체 조회
        User user = ((CustomUserDetails) customUserDetailsService.loadUserByUserId(email)).getUser();
        Long clubUserId = user.getId();

        return recruitService.getRecruitHome(recruitId, clubId, clubUserId);
    }
}