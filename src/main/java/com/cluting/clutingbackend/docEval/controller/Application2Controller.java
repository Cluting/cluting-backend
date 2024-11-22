package com.cluting.clutingbackend.docEval.controller;

import com.cluting.clutingbackend.docEval.dto.ApplicationResponse;
import com.cluting.clutingbackend.docEval.service.Application2Service;
import com.cluting.clutingbackend.plan.domain.User;
import com.cluting.clutingbackend.util.security.CustomUserDetails;
import com.cluting.clutingbackend.util.security.CustomUserDetailsService;
import com.cluting.clutingbackend.util.security.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "서류평가", description = "서류 평가 관련 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class Application2Controller {

    private final Application2Service applicationService;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Operation(
            summary = "[서류평가하기] 평가 전/중/후 화면 불러오기",
            description = "클럽 ID와 게시물 ID를 기반으로 평가 상태에 따른 지원자 리스트 정보를 불러옵니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 지원자 리스트를 반환합니다."),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
                    @ApiResponse(responseCode = "401", description = "인증 실패: 유효하지 않은 토큰입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
            }
    )
    @GetMapping("/doc")
    public ResponseEntity<List<ApplicationResponse>> getApplicationList(
            @RequestParam Long clubId,
            @RequestParam Long postId,
            @RequestHeader("Authorization") String token) {

        // 토큰에서 이메일 추출
        String email = jwtProvider.getUserEmail(token);

        // 이메일을 통해 User 객체 조회
        User user = ((CustomUserDetails) customUserDetailsService.loadUserByUserId(email)).getUser();
        Long clubUserId = user.getId();

        List<ApplicationResponse> response = applicationService.getApplicationList(clubId, postId, clubUserId);
        return ResponseEntity.ok(response);
    }
}