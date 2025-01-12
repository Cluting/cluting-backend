package com.cluting.clutingbackend.application.controller;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.dto.GroupSelectRequestDto;
import com.cluting.clutingbackend.application.dto.request.AnswerSaveRequestDto;
import com.cluting.clutingbackend.application.dto.request.ApplicantProfileRequestDto;
import com.cluting.clutingbackend.application.dto.response.*;
import com.cluting.clutingbackend.application.service.ApplicationDetailService;
import com.cluting.clutingbackend.application.service.ApplicationService;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.plan.domain.DocumentAnswer;
import com.cluting.clutingbackend.plan.domain.DocumentQuestion;
import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.user.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/applicant")
@Tag(name = "지원자 프로필 및 지원 내역 API")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final ApplicationDetailService applicationDetailService;

    // [지원서 작성하기] 지원자 정보 조회하기(프로필, 이름, 번호, 이메일, 거주지, 학교, 학과, 다전공)
    @Operation(summary = "[지원서 작성하기] 지원자 정보 조회하기",description = "지원자의 정보를 조회합니다.")
    @GetMapping("/my")
    public UserResponseDto findUserInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails){
        return applicationService.findUserInfo(userDetails.getUser());
    }

    // [지원서 작성하기] 모집 그룹 목록 조회하기
    @Operation(summary = "[지원서 작성하기] 모집 그룹 목록 조회하기",description = "모집 공고의 모집 그룹을 조회합니다.")
    @GetMapping("/{recruitId}/group")
    public List<String> findGroups(
            @PathVariable("recruitId") Long recruitId){
        return applicationService.findGroups(recruitId);
    }

    // [지원서 작성하기] 모집 그룹 선택 저장하기 - Application 생성 및 사용자,모집공고,파트 초기 저장
    @Operation(summary = "[지원서 작성하기] 모집 그룹 선택 저장하기",description = "모집 공고의 모집 그룹 선택을 저장합니다.")
    @PostMapping("/{recruitId}/group")
    public ResponseEntity<Void> selectGroup(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("recruitId") Long recruitId,
            @RequestBody GroupSelectRequestDto groupSelectRequestDto){
        applicationService.selectGroup(userDetails.getUser(), recruitId, groupSelectRequestDto);
        return ResponseEntity.ok().build();
    }

    // [지원서 작성하기] 공통 질문 조회하기
    @Operation(summary = "[지원서 작성하기] 공통 질문 조회하기", description = "공통 질문을 조회합니다.")
    @GetMapping("/{recruitId}/common")
    public List<DocumentQuestionResponseDto> findCommonQuestion(
            @PathVariable("recruitId") Long recruitId){
        return applicationService.findCommonQuestion(recruitId);
    }

    // [지원서 작성하기] 공통 질문 답변 저장하기
    @Operation(summary = "[지원서 작성하기] 공통 질문 답변 저장하기",description = "공통 질문 답변을 저장합니다.")
    @PostMapping("/{recruitId}/common")
    public ResponseEntity<Void> saveCommonAnswer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("recruitId") Long recruitId,
            @RequestBody AnswerSaveRequestDto answerSaveRequestDto){
        applicationService.saveCommonAnswer(userDetails.getUser(), recruitId, answerSaveRequestDto);
        return ResponseEntity.ok().build();
    }

    // [지원서 작성하기] 파트별(파트가 2개 이상일 때에는 모든 질문) 질문 조회하기
    // [지원서 작성하기] 파트별(파트가 2개 이상일 때에는 모든 질문) 질문 답변 저장하기
    // [지원서 작성하기] 파일 제출일 경우 파일 저장
    // [지원서 작성하기] 지원자의 포트폴리오 url 조회 및 운영진들의 면접 가능 시간 조회
    // [지원서 작성하기] 지원자의 포트폴리오 url 입력 저장 및 운영진들의 면접 가능 시간 기반의 지원자의 면접 가능 시간 선택 저장
    // [지원서 작성하기] 제출 확정하기 - createdAt 저장

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

    @Operation(summary = "동아리 공고에 대한 지원 상세 보기")
    @GetMapping("/application-detail/{recruitId}")
    public ResponseEntity<ApplicationDetailResponseDto> getApplicationDetails(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable(name = "recruitId") Long recruitId) {
        Long userId = customUserDetails.getId(); // CustomUserDetails에서 유저 ID 가져오기
        ApplicationDetailResponseDto response = applicationDetailService.getApplicationDetail(userId, recruitId);
        return ResponseEntity.ok(response);
    }
}
