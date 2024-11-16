package com.cluting.clutingbackend.invite.controller;

import com.cluting.clutingbackend.invite.domain.AdminInvite;
import com.cluting.clutingbackend.invite.service.AdminInviteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/recruiting/admin")
public class AdminInviteController {

    private final AdminInviteService adminInviteService;

    public AdminInviteController(AdminInviteService adminInviteService) {
        this.adminInviteService = adminInviteService;
    }

    // 초대 링크 생성 엔드포인트
    @PostMapping("/invite")
    public ResponseEntity<?> createInviteLink(@RequestParam Long clubId) {
        String inviteLink = adminInviteService.createInviteLink(clubId);
        return ResponseEntity.ok().body(Map.of("inviteLink", inviteLink));
    }

    // 초대 링크를 통한 초대 확인
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
}
