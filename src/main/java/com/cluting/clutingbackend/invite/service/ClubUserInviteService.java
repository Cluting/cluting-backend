package com.cluting.clutingbackend.invite.service;

import com.cluting.clutingbackend.invite.domain.AdminInvite;
import com.cluting.clutingbackend.invite.repository.AdminInviteRepository;
import com.cluting.clutingbackend.invite.repository.ClubInviteRepository;
import com.cluting.clutingbackend.invite.repository.ClubUserInviteRepository;
import com.cluting.clutingbackend.invite.repository.UserInviteRepository;
import com.cluting.clutingbackend.plan.domain.Club;
import com.cluting.clutingbackend.plan.domain.ClubUser;
import com.cluting.clutingbackend.plan.domain.User;
import com.cluting.clutingbackend.plan.repository.ClubRepository;
import com.cluting.clutingbackend.plan.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClubUserInviteService {

    private final UserInviteRepository userRepository;
    private final ClubUserInviteRepository clubUserRepository;
    private final ClubInviteRepository clubRepository;
    private final AdminInviteRepository adminInviteRepository;

    public ClubUserInviteService(UserInviteRepository userRepository, ClubUserInviteRepository clubUserRepository, ClubInviteRepository clubRepository, AdminInviteRepository adminInviteRepository) {
        this.userRepository = userRepository;
        this.clubUserRepository = clubUserRepository;
        this.clubRepository = clubRepository;
        this.adminInviteRepository = adminInviteRepository;
    }

    @Transactional
    public String addUserByInviteToken(String token) {
        // 초대 정보 조회 및 유효성 검사
        AdminInvite invite = adminInviteRepository.findByUniqueInviteTokenAndIsUsedFalse(token)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않거나 사용된 초대 링크입니다."));

        if (invite.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("초대 링크가 만료되었습니다.");
        }

        // User 엔티티 생성
        User newUser = User.builder().build(); // nullable 필드 모두 생략 가능
        userRepository.save(newUser);

        // Club 조회
        Club club = clubRepository.findById(invite.getClubId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 클럽입니다."));

        // ClubUser 생성 및 저장
        ClubUser clubUser = new ClubUser();
        clubUser.setUser(newUser);
        clubUser.setClub(club);
        clubUser.setRole(ClubUser.Role.STAFF);
        clubUserRepository.save(clubUser);

        // 초대 링크 사용 처리
        invite.markAsUsed();
        adminInviteRepository.save(invite);


        // 사용자 ID와 클럽 ID를 포함한 메시지 반환
        return String.format("초대된 사용자(%d)가 동아리(%d)의 운영진으로 추가되었습니다.", newUser.getId(), club.getId());
    }
}

