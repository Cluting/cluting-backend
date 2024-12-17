package com.cluting.clutingbackend.application.controller;


import com.cluting.clutingbackend.application.dto.response.ClubResponseDto;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.application.service.ApplicationService;
import com.cluting.clutingbackend.global.annotation.RequiredPermission;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applicant")
@Tag(name = "지원자 관련 API 모음")
@RequiredArgsConstructor
public class RecruitListController {

    private final ApplicationService applicationService;
    private static final String RECENT_CLUBS_COOKIE_NAME = "recentClubs";

    @GetMapping("/recruitList/1")
    @Operation(summary = "지원 중인 공고 리스트")
    public ResponseEntity<List<ClubResponseDto>> showApplicatingRecruitList(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(applicationService.getApplyingClubs(userDetails));
    }
    @GetMapping("/recruitList/2")
    @Operation(summary="지원한 공고 리스트")
    public ResponseEntity<List<ClubResponseDto>> showApplicatedRecruitList(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(applicationService.getAppliedClubs(userDetails));
    }
    @GetMapping("/recruitList/3")
    @Operation(summary = "스크랩한 동아리 공고 리스트")
    public ResponseEntity<List<ClubResponseDto>> showScrappedRecruitList(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(applicationService.getScrapedClubs(userDetails));

    }
//    @GetMapping("/recruitList/4")
//    @Operation(summary = "최근 본 동아리 공고 리스트")
//    public ResponseEntity<List<ClubResponseDto>> showRecentRecruitList(@AuthenticationPrincipal CustomUserDetails userDetails){
//        return ResponseEntity.ok(applicationService.getRecentClubs(userDetails));
//
//    }
}
