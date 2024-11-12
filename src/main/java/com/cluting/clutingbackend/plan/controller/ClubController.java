package com.cluting.clutingbackend.plan.controller;

import com.cluting.clutingbackend.plan.dto.request.ClubCreateRequestDto;
import com.cluting.clutingbackend.plan.dto.response.ClubResponseDto;
import com.cluting.clutingbackend.plan.service.ClubService;
import com.cluting.clutingbackend.util.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/club")
public class ClubController {
    private final ClubService clubService;

    // 가장 인기 있는 동아리

    // 동아리 추가
    @PostMapping("/create")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ClubResponseDto create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute ClubCreateRequestDto clubCreateRequestDto,
            @RequestPart(value = "profile", required = false) MultipartFile profile) {
        return clubService.create(userDetails.getUser(), clubCreateRequestDto, profile);
    }

    // 리크루팅 시작

}
