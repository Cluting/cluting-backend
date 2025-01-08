package com.cluting.clutingbackend.clubuser.service;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.clubuser.dto.response.ClubUserResponseDto;
import com.cluting.clutingbackend.clubuser.repository.ClubUserRepository;
import com.cluting.clutingbackend.global.exception.CustomException;
import com.cluting.clutingbackend.global.exception.ErrorCode;
import com.cluting.clutingbackend.global.security.CustomAuthenticationToken;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubUserService {
    private final ClubUserRepository clubUserRepository;

    @Transactional(readOnly = true)
    public List<ClubUserResponseDto> findAll(Long clubId) {
        return clubUserRepository.findByClubId(clubId).stream().map(ClubUserResponseDto::toDto).toList();
    }

    public ClubUser getCurrentClubUser(Long clubUserId){
        // 현재 사용자 정보 가져오기
        CustomUserDetails currentUserDetails =
                (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 선택한 ClubUser 가져오기
        ClubUser selectedClubUser = clubUserRepository.findById(clubUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.CLUB_USER_NOT_FOUND," | ClubUser not found with ID: " + clubUserId));

        // 선택된 ClubUser를 CustomUserDetails에 반영
        CustomUserDetails updatedDetails = new CustomUserDetails(currentUserDetails.getUser(), selectedClubUser);

        // SecurityContext에 업데이트된 사용자 정보 저장
        SecurityContextHolder.getContext().setAuthentication(
                new CustomAuthenticationToken(updatedDetails, updatedDetails.getAuthorities())
        );

        return selectedClubUser;
    }
}
