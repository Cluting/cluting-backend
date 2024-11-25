package com.cluting.clutingbackend.docEval.controller;

import com.cluting.clutingbackend.docEval.dto.EvaluationRequest;
import com.cluting.clutingbackend.docEval.dto.EvaluationResponse;
import com.cluting.clutingbackend.docEval.service.Evaluation2Service;
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

@Tag(name = "서류평가", description = "서류 평가 관련 API")
@RestController
@RequestMapping("/api/v1/doc")
@RequiredArgsConstructor
public class Evaluation2Controller {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final Evaluation2Service evaluationService;

    @Operation(
            summary = "서류평가 세부 정보 조회",
            description = "지원자의 서류 평가를 위해 세부 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 평가 세부 정보를 반환합니다."),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
                    @ApiResponse(responseCode = "401", description = "인증 실패: 유효하지 않은 토큰입니다."),
                    @ApiResponse(responseCode = "404", description = "지원자 정보를 찾을 수 없습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
            }
    )
    @GetMapping("/evaluate/{applicationId}")
    public ResponseEntity<EvaluationResponse> getEvaluationDetails(
            @PathVariable Long applicationId,
            @RequestParam Long clubId,
            @RequestParam Long postId,
            @RequestParam String partName,
            @RequestHeader("Authorization") String token) {


        // 토큰에서 이메일 추출
        String email = jwtProvider.getUserEmail(token);

        // 이메일을 통해 User 객체 조회
        User user = ((CustomUserDetails) customUserDetailsService.loadUserByUserId(email)).getUser();
        Long clubUserId = user.getId();

        EvaluationResponse response = evaluationService.getEvaluationDetails(applicationId, clubId, postId, partName, clubUserId);
        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "서류평가 제출",
            description = "지원자의 서류를 평가하고 결과를 제출합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "서류 평가가 성공적으로 제출되었습니다."),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
                    @ApiResponse(responseCode = "401", description = "인증 실패: 유효하지 않은 토큰입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
            }
    )
    @PostMapping("/evaluate/{applicationId}")
    public ResponseEntity<String> evaluateDocument(
            @PathVariable Long applicationId,
            @RequestParam Long clubId,
            @RequestParam Long postId,
            @RequestParam String partName,
            @RequestHeader("Authorization") String token,
            @RequestBody EvaluationRequest evaluationRequest) {

        // 토큰에서 이메일 추출
        String email = jwtProvider.getUserEmail(token);

        // 이메일을 통해 User 객체 조회
        User user = ((CustomUserDetails) customUserDetailsService.loadUserByUserId(email)).getUser();
        Long clubUserId = user.getId();

        evaluationService.evaluateDocument(applicationId, clubId, postId, partName, clubUserId, evaluationRequest);

        return ResponseEntity.ok("Evaluation submitted successfully");
    }
}