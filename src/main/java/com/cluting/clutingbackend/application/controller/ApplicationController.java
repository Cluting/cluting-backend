package com.cluting.clutingbackend.application.controller;

import com.cluting.clutingbackend.application.dto.response.ApplicationStatusResponseDto;
import com.cluting.clutingbackend.application.service.ApplicationService;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class ApplicationController {
    private final ApplicationService applicationService;

    @GetMapping
    public ResponseEntity<List<ApplicationStatusResponseDto>> showProfileHome(@AuthenticationPrincipal CustomUserDetails userDetails){
        List<ApplicationStatusResponseDto> response = applicationService.getApplicationStatusAndCalendar(userDetails);
        return ResponseEntity.ok(response);
    }
}
