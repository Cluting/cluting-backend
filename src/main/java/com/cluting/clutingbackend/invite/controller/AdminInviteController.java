package com.cluting.clutingbackend.invite.controller;

import com.cluting.clutingbackend.invite.domain.AdminInvite;
import com.cluting.clutingbackend.invite.service.AdminInviteService;
import com.cluting.clutingbackend.invite.service.ClubUserInviteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;


@Tag(name = "운영진 초대", description = "운영진 초대 관련 API")
@RestController
@RequestMapping("/api/v1/recruiting/admin")
public class AdminInviteController {

    private final AdminInviteService adminInviteService;

    private final ClubUserInviteService clubUserService;

    public AdminInviteController(AdminInviteService adminInviteService, ClubUserInviteService clubUserService) {
        this.adminInviteService = adminInviteService;
        this.clubUserService = clubUserService;
    }


    // 초대 링크 생성 엔드포인트
    @Operation(
            summary = "초대 링크 생성",
            description = "클럽 ID를 기반으로 운영진 초대 링크를 생성합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "초대 링크가 성공적으로 생성되었습니다."),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
            }
    )
    @PostMapping("/invite")
    public ResponseEntity<?> createInviteLink(@RequestParam Long clubId) {
        String inviteLink = adminInviteService.createInviteLink(clubId);
        return ResponseEntity.ok().body(Map.of("inviteLink", inviteLink));
    }


    // 초대 링크를 통한 초대 확인
    @Operation(
            summary = "초대 링크 확인",
            description = "초대 링크를 검증하고, 초대를 확인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "초대가 성공적으로 확인되었습니다."),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 예: 초대 링크가 만료됨."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
            }
    )
    @GetMapping("/invite")
    public ResponseEntity<?> verifyInviteLink(@RequestParam String token) {
        try {
            AdminInvite invite = adminInviteService.getInviteByToken(token);

            if (invite.getExpirationDate().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("초대 링크가 만료되었습니다.");
            }

            adminInviteService.markInviteAsUsed(invite);
            return ResponseEntity.ok("초대가 확인되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Operation(
            summary = "초대 링크 수락",
            description = "초대 링크를 통해 운영진으로 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "운영진으로 성공적으로 추가되었습니다."),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
            }
    )
    @PostMapping("/accept-invite")
    public ResponseEntity<?> acceptInvite(@RequestParam String token) {
        try {
            String message = clubUserService.addUserByInviteToken(token);
            return ResponseEntity.ok(message); // 메시지 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
