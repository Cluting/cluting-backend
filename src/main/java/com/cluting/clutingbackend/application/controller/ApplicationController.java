package com.cluting.clutingbackend.application.controller;

import com.cluting.clutingbackend.application.dto.request.ApplicantProfileRequestDto;
import com.cluting.clutingbackend.application.dto.response.ApplicantProfileResponseDto;
import com.cluting.clutingbackend.application.dto.response.ApplicationStatusResponseDto;
import com.cluting.clutingbackend.application.service.ApplicationService;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/applicant")
@Tag(name = "지원자 관련 API 모음")
public class ApplicationController {
    private final ApplicationService applicationService;


    @Operation(summary = "지원자 프로필 홈",description = "내 지원 상황 및 지원 캘린더를 확인할 수 있습니다")
    @GetMapping("/home")
    public ResponseEntity<List<ApplicationStatusResponseDto>> showProfileHome(@AuthenticationPrincipal CustomUserDetails userDetails){
        List<ApplicationStatusResponseDto> response = applicationService.getApplicationStatusAndCalendar(userDetails);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "기본 프로필 설정",description = "지원자의 정보를 열람할 수 있습니다")
    @GetMapping("/profile")
    public ResponseEntity<ApplicantProfileResponseDto> getProfileInfo(@AuthenticationPrincipal CustomUserDetails userDetails){
        ApplicantProfileResponseDto dto = applicationService.getInfo(userDetails);
        return ResponseEntity.ok().body(dto);
    }

    @Operation(summary = "기본 프로필 설정 - 수정",description = "지원자의 정보를 수정할 수 있습니다")
    @PutMapping("/profile")
    public ResponseEntity<String> modifyProfileInfo(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ApplicantProfileRequestDto requestDto){
         String res = applicationService.changeUserInfo(userDetails,requestDto);
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "기본 프로필 설정 - 저장",description = "지원자의 포트폴리오를 저장할 수 있습니다")
    @PutMapping("/portfolio")
    public ResponseEntity<String> savePortfolio(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ApplicantProfileRequestDto requestDto){
        String res = applicationService.changeUserInfo(userDetails,requestDto);
        return ResponseEntity.ok().body(res);
    }
}
