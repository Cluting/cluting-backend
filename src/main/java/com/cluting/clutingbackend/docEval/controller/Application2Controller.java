package com.cluting.clutingbackend.docEval.controller;

import com.cluting.clutingbackend.docEval.dto.ApplicationResponse;
import com.cluting.clutingbackend.docEval.service.Application2Service;
import com.cluting.clutingbackend.plan.domain.User;
import com.cluting.clutingbackend.util.security.CustomUserDetails;
import com.cluting.clutingbackend.util.security.CustomUserDetailsService;
import com.cluting.clutingbackend.util.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class Application2Controller {

    private final Application2Service applicationService;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

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