package com.cluting.clutingbackend.docEval.controller;

import com.cluting.clutingbackend.docEval.dto.ApplicationDto;
import com.cluting.clutingbackend.docEval.service.DocumentCompletionService;
import com.cluting.clutingbackend.plan.domain.User;
import com.cluting.clutingbackend.util.security.CustomUserDetails;
import com.cluting.clutingbackend.util.security.CustomUserDetailsService;
import com.cluting.clutingbackend.util.security.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/doc")
public class DocumentCompletionController {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final DocumentCompletionService documentCompletionService;

    public DocumentCompletionController(JwtProvider jwtProvider, CustomUserDetailsService customUserDetailsService, DocumentCompletionService documentCompletionService) {
        this.jwtProvider = jwtProvider;
        this.customUserDetailsService = customUserDetailsService;
        this.documentCompletionService = documentCompletionService;
    }

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
}