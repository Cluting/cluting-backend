package com.cluting.clutingbackend.docEval.controller;

import com.cluting.clutingbackend.docEval.dto.EvaluationResponse;
import com.cluting.clutingbackend.docEval.service.Evaluation2Service;
import com.cluting.clutingbackend.plan.domain.User;
import com.cluting.clutingbackend.util.security.CustomUserDetails;
import com.cluting.clutingbackend.util.security.CustomUserDetailsService;
import com.cluting.clutingbackend.util.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/doc")
@RequiredArgsConstructor
public class Evaluation2Controller {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final Evaluation2Service evaluationService;

    @GetMapping("/evaluate/{applicationId}")
    public ResponseEntity<EvaluationResponse> getEvaluationDetails(
            @PathVariable Long applicationId,
            @RequestParam Long clubId,
            @RequestParam Long postId,
            @RequestHeader("Authorization") String token) {


        // 토큰에서 이메일 추출
        String email = jwtProvider.getUserEmail(token);

        // 이메일을 통해 User 객체 조회
        User user = ((CustomUserDetails) customUserDetailsService.loadUserByUserId(email)).getUser();
        Long clubUserId = user.getId();

        EvaluationResponse response = evaluationService.getEvaluationDetails(applicationId, clubId, postId, clubUserId);
        return ResponseEntity.ok(response);
    }
}