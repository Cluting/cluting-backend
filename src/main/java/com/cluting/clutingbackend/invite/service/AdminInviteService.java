package com.cluting.clutingbackend.invite.service;

import com.cluting.clutingbackend.invite.domain.AdminInvite;
import com.cluting.clutingbackend.invite.repository.AdminInviteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AdminInviteService {

    private final AdminInviteRepository adminInviteRepository;

    public AdminInviteService(AdminInviteRepository adminInviteRepository) {
        this.adminInviteRepository = adminInviteRepository;
    }

    @Transactional
    public String createInviteLink(Long clubId) {
        // unique_invite_token 생성
        String uniqueToken = UUID.randomUUID().toString();

        // 유효 기간 설정 (예: 현재 시간부터 7일 후 만료)
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(7);

        // AdminInvite 엔티티 생성 및 저장
        AdminInvite adminInvite = new AdminInvite(uniqueToken, clubId, expirationDate);
        adminInviteRepository.save(adminInvite);

        // 초대 링크 반환
        return "https://cluting.com/invite?token=" + uniqueToken;
    }

    @Transactional(readOnly = true)
    public AdminInvite getInviteByToken(String token) {
        return adminInviteRepository.findByUniqueInviteTokenAndIsUsedFalse(token)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않거나 사용된 초대 링크입니다."));
    }

    @Transactional
    public void markInviteAsUsed(AdminInvite invite) {
        invite.markAsUsed();
        adminInviteRepository.save(invite);
    }
}
