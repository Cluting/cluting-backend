package com.cluting.clutingbackend.admininvite.service;

import com.cluting.clutingbackend.admininvite.domain.AdminInvite;
import com.cluting.clutingbackend.admininvite.domain.TempUser;
import com.cluting.clutingbackend.admininvite.dto.AdminInviteAcceptRequestDto;
import com.cluting.clutingbackend.admininvite.dto.AdminInviteRequestDto;
import com.cluting.clutingbackend.admininvite.dto.AdminInviteResponseDto;
import com.cluting.clutingbackend.admininvite.repository.AdminInviteRepository;
import com.cluting.clutingbackend.admininvite.repository.TempUserRepository;
import com.cluting.clutingbackend.club.domain.Club;
import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.clubuser.repository.ClubUserRepository;
import com.cluting.clutingbackend.global.enums.ClubRole;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminInviteService {
    private final AdminInviteRepository adminInviteRepository;
    private final UserRepository userRepository;
    private final TempUserRepository tempUserRepository;
    private final ClubUserRepository clubUserRepository;

    // [운영진 초대] 초대 링크 생성
    @Transactional
    public String generateInviteLink(AdminInviteRequestDto requestDto, Club club) {
        String token = UUID.randomUUID().toString();
        AdminInvite adminInvite = AdminInvite.builder()
                .uniqueInviteToken(token)
                .club(club)
                .expirationDate(LocalDateTime.now().plusDays(7))
                .isUsed(false)
                .build();
        adminInviteRepository.save(adminInvite);

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        return baseUrl + "/api/v1/admin/invite?token=" + token;
    }

    // [운영진 초대] 초대 수락(회원/비회원에 따라 로직이 달라짐)
    @Transactional
    public AdminInviteResponseDto acceptInvite(AdminInviteAcceptRequestDto requestDto) {
        AdminInvite adminInvite = adminInviteRepository.findByUniqueInviteToken(requestDto.getToken())
                .orElseThrow(() -> new IllegalArgumentException("만료된 토큰이거나 유효하지 않은 토큰"));

        if (adminInvite.getIsUsed() || adminInvite.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("만료된 토큰이거나 이미 사용된 토큰");
        }

        // 회원인지, 비회원인지
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseGet(() -> handleTemporaryUser(requestDto, adminInvite)); // 비회원일 때

        Club club = adminInvite.getClub();
        System.out.println("@@club : "+club.getName());

        // 기수 가져오기
        Recruit recruit = club.getRecruits().stream()
                .filter(r -> !r.getIsDone())  //현재 활성화된 공고만
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("모집 공고를 찾지 못함"));

        ClubUser clubUser = ClubUser.builder()
                .user(user)
                .club(club)
                .role(ClubRole.STAFF)
                .generation(recruit.getGeneration())
                .build();
        clubUserRepository.save(clubUser);

        // 토큰 만료 상태로 변경
        adminInvite.setIsUsed(true);
        adminInviteRepository.save(adminInvite);

        return new AdminInviteResponseDto(club.getName(), recruit.getGeneration());
    }

    // [운영진 초대] 비회원 로직 처리
    private User handleTemporaryUser(AdminInviteAcceptRequestDto requestDto, AdminInvite adminInvite) {
        TempUser tempUser = TempUser.builder()
                .email(requestDto.getEmail())
                .invite(adminInvite)
                .build();
        tempUserRepository.save(tempUser);

        throw new IllegalArgumentException("비회원. 회원가입 페이지로 다이렉트 바람.");
    }
}
