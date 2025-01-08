package com.cluting.clutingbackend.global.controller;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.clubuser.service.ClubUserService;
import com.cluting.clutingbackend.global.enums.CurrentStage;
import com.cluting.clutingbackend.global.exception.CustomException;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.global.service.CurrentStageService;
import com.cluting.clutingbackend.recruit.dto.response.CurrentStageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.cluting.clutingbackend.global.exception.ErrorCode.CLUB_USER_NOT_FOUND;

@Tag(name = "전역적으로 제공해야 하는 API 모음")
@RestController
@RequestMapping("/api/v1/global")
@RequiredArgsConstructor
public class GlobalController {

    private final CurrentStageService currentStageService;
    private final ClubUserService clubUserService;

    @Operation(
            summary = "[리크루팅 공고 진행 단계] 현재 단계(전/중/후) 불러오기"
    )
    @GetMapping("/current-stage")
    public ResponseEntity<CurrentStageResponseDto> getCurrentStage(@RequestParam Long recruitId){

        return ResponseEntity.ok(currentStageService.getCurrentStage(recruitId));
    }

    @Operation(summary = "프로필 선택에 따라 User의 ClubUser역할이 바뀌게끔 함")
    @PostMapping("/select/{clubUserId}")
    public ResponseEntity<List<Map<String,Object>>> selectClubUser(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long clubUserId) {
        ClubUser clubUser = clubUserService.getCurrentClubUser(clubUserId);
        if(clubUser == null){
            throw new CustomException(CLUB_USER_NOT_FOUND,"| 요청한 clubUserId : " + clubUserId);
        }

        List<Map<String,Object>> response = new ArrayList<Map<String,Object>>();
        response.add(Map.of("User Result : ",userDetails.getUser().toString()));
        response.add(Map.of("ClubUser Result : ",clubUser.toString()));

        System.out.println(clubUser.toString());
        System.out.println(userDetails.getUser().toString());

        return ResponseEntity.ok(response);
    }
}
