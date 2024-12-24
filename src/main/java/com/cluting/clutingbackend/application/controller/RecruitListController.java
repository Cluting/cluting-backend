package com.cluting.clutingbackend.application.controller;


import com.cluting.clutingbackend.application.dto.response.ClubResponseDto;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.application.service.ApplicationService;
import com.cluting.clutingbackend.application.service.CookieService;
import com.cluting.clutingbackend.global.annotation.RequiredPermission;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/applicant")
@Tag(name = "지원자 관련 API 모음")
@RequiredArgsConstructor
public class RecruitListController {

    private final ApplicationService applicationService;
    private final CookieService cookieService;

    @GetMapping("/recruitList/applying")
    @Operation(summary = "지원 중인 공고 리스트")
    public ResponseEntity<List<ClubResponseDto>> showApplyingRecruitList(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(applicationService.getApplyingClubs(userDetails));
    }
    @GetMapping("/recruitList/applied")
    @Operation(summary="지원한 공고 리스트")
    public ResponseEntity<List<ClubResponseDto>> showAppliedRecruitList(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(applicationService.getAppliedClubs(userDetails));
    }
    @GetMapping("/recruitList/scrapped")
    @Operation(summary = "스크랩한 동아리 공고 리스트")
    public ResponseEntity<List<ClubResponseDto>> showScrappedRecruitList(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(applicationService.getScrapedClubs(userDetails));
    }
    @GetMapping("/recruitList/recent")
    @Operation(summary = "최근에 본 동아리 공고 리스트")
    public ResponseEntity<List<ClubResponseDto>> getRecentRecruits(HttpServletRequest request) throws IOException {
        // 서비스 레이어 호출하여 쿠키에서 최근 본 공고 조회
        List<ClubResponseDto> recentRecruits = cookieService.getRecentRecruitsFromCookie(request);
        return ResponseEntity.ok(recentRecruits);
    }

    @GetMapping("/recruitList/passed")
    @Operation(summary = "나의 지원 기록 - 합격한 동아리")
    public ResponseEntity<List<ClubResponseDto>> getPassedClubs(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ClubResponseDto> passedClubs = applicationService.getPassedClubs(userDetails.getId());
        return ResponseEntity.ok(passedClubs);
    }

    @GetMapping("/recruitList/failed")
    @Operation(summary = "나의 지원 기록 - 불합격한 동아리")
    public ResponseEntity<List<ClubResponseDto>> getFailedClubs(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ClubResponseDto> failedClubs = applicationService.getFailedClubs(userDetails.getId());
        return ResponseEntity.ok(failedClubs);
    }
}
