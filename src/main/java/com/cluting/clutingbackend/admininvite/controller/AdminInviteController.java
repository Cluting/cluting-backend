package com.cluting.clutingbackend.admininvite.controller;

import com.cluting.clutingbackend.admininvite.dto.AdminInviteAcceptRequestDto;
import com.cluting.clutingbackend.admininvite.dto.AdminInviteRequestDto;
import com.cluting.clutingbackend.admininvite.dto.AdminInviteResponseDto;
import com.cluting.clutingbackend.admininvite.service.AdminInviteService;
import com.cluting.clutingbackend.club.domain.Club;
import com.cluting.clutingbackend.club.repository.ClubRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[리크루팅 홈] 운영진 초대", description = "운영진 초대 관련 API")
@RestController
@RequestMapping("/api/v1/admin/invite")
@RequiredArgsConstructor
public class AdminInviteController {
    private final AdminInviteService adminInviteService;
    private final ClubRepository clubRepository;

    // [운영진 초대] 초대 링크 생성
    @Operation(
            summary = "초대 링크 생성",
            description = "클럽 ID를 기반으로 운영진 초대 링크를 생성합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "초대 링크가 성공적으로 생성되었습니다."),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
            }
    )
    @PostMapping
    public ResponseEntity<String> generateInviteLink(@RequestBody AdminInviteRequestDto requestDto) {
        Club club = clubRepository.findById(requestDto.getClubId())
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));
        String link = adminInviteService.generateInviteLink(requestDto, club);
        return ResponseEntity.ok(link);
    }

    // [운영진 초대] 초대 수락
    @Operation(
            summary = "초대 링크 수락",
            description = "초대 링크를 통해 운영진으로 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "운영진으로 성공적으로 추가되었습니다."),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
            }
    )
    @PostMapping("/accept")
    public ResponseEntity<AdminInviteResponseDto> acceptInvite(@RequestBody AdminInviteAcceptRequestDto requestDto) {
        AdminInviteResponseDto responseDto = adminInviteService.acceptInvite(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
