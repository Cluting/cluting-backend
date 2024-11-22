package com.cluting.clutingbackend.docEval.controller;

import com.cluting.clutingbackend.docEval.dto.ApplicationDto;
import com.cluting.clutingbackend.docEval.dto.ApplicationStateRequest;
import com.cluting.clutingbackend.docEval.service.Application2Service;
import com.cluting.clutingbackend.docEval.service.DocumentCompletionService;
import com.cluting.clutingbackend.plan.domain.User;
import com.cluting.clutingbackend.util.security.CustomUserDetails;
import com.cluting.clutingbackend.util.security.CustomUserDetailsService;
import com.cluting.clutingbackend.util.security.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "서류평가", description = "서류 평가 관련 API")
@RestController
@RequestMapping("/api/v1/doc")
public class DocumentCompletionController {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final DocumentCompletionService documentCompletionService;
    private final Application2Service applicationService;

    public DocumentCompletionController(JwtProvider jwtProvider, CustomUserDetailsService customUserDetailsService, DocumentCompletionService documentCompletionService, Application2Service applicationService) {
        this.jwtProvider = jwtProvider;
        this.customUserDetailsService = customUserDetailsService;
        this.documentCompletionService = documentCompletionService;
        this.applicationService = applicationService;
    }

    @Operation(
            summary = "서류평가 완료 후 지원자 리스트 조회",
            description = "클럽 ID와 게시물 ID를 기반으로 서류 평가 후 합격/불합격 지원자 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 완료된 지원자 목록을 반환합니다."),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
                    @ApiResponse(responseCode = "401", description = "인증 실패: 유효하지 않은 토큰입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
            }
    )
    @GetMapping("/complete")
    public ResponseEntity<Map<String, List<ApplicationDto>>> getCompletedApplications(
            @RequestParam Long clubId,
            @RequestParam Long postId,
            @RequestHeader("Authorization") String token) {

        // 토큰에서 이메일 추출
        String email = jwtProvider.getUserEmail(token);

        // 이메일을 통해 User 객체 조회
        User user = ((CustomUserDetails) customUserDetailsService.loadUserByUserId(email)).getUser();
        Long clubUserId = user.getId();

        Map<String, List<ApplicationDto>> response = documentCompletionService.getCompletedApplications(postId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "서류평가 완료하기",
            description = "지원자 상태(합격/불합격)를 업데이트합니다. 클럽 ID, 게시물 ID와 상태 정보가 필요합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "지원자 상태가 성공적으로 업데이트되었습니다."),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
            }
    )
    @PostMapping("/complete")
    public ResponseEntity<String> updateApplicationState(
            @RequestParam Long clubId,
            @RequestParam Long postId,
            @RequestBody List<ApplicationStateRequest> applicationStateRequests) {
        applicationService.updateApplicationStates(clubId, postId, applicationStateRequests);
        return ResponseEntity.ok("Application states updated successfully.");
    }
}