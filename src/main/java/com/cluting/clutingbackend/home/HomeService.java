package com.cluting.clutingbackend.home;

import com.cluting.clutingbackend.plan.domain.ClubUser;
import com.cluting.clutingbackend.plan.domain.User;
import com.cluting.clutingbackend.plan.dto.response.ClubResponseDto;
import com.cluting.clutingbackend.plan.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final ClubRepository clubRepository;

    // 로그인 된 사용자가 입한 동아리 목록
    @Transactional(readOnly = true)
    public List<ClubResponseDto> userClubs(User user) {
        List<ClubUser> clubUsers = user.getClubUsers();
        return clubUsers.stream().map(clubUser -> ClubResponseDto.toDto(clubUser.getClub())).toList();
    }

    // 동아리 id로 조회
    @Transactional(readOnly = true)
    public ClubResponseDto findClub(Long clubId) {
        return ClubResponseDto.toDto(
                clubRepository.findById(clubId).orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "존재하지 않는 동아리 입니다."
                        )
                )
        );
    }
}
