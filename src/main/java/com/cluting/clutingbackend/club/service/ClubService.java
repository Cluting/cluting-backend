package com.cluting.clutingbackend.club.service;

import com.cluting.clutingbackend.club.domain.Club;
import com.cluting.clutingbackend.club.dto.request.ClubRegisterRequestDto;
import com.cluting.clutingbackend.club.dto.response.ClubResponseDto;
import com.cluting.clutingbackend.club.repository.ClubRepository;
import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.clubuser.repository.ClubUserRepository;
import com.cluting.clutingbackend.global.s3.AwsS3Service;
import com.cluting.clutingbackend.global.util.StaticValue;
import com.cluting.clutingbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final ClubUserRepository clubUserRepository;
    private final AwsS3Service awsS3Service;

    // 가장 인기있는 동아리 조회
    @Transactional(readOnly = true)
    public List<ClubResponseDto> popular(User user) {
        return clubRepository.findPopular().stream().map(ClubResponseDto::toDto).toList();
    }

    // 동아리 등록
    @Transactional
    public ClubResponseDto registerClub(User user, ClubRegisterRequestDto clubRegisterRequestDto, MultipartFile profile) {
        String imageUrl;
        if (profile == null) imageUrl = StaticValue.profileImage;
        else imageUrl = awsS3Service.uploadFile(profile);

        return ClubResponseDto.toDto(clubRepository.save(clubRegisterRequestDto.toEntity(imageUrl)));
    }

    // 동아리 id로 조회
    @Transactional(readOnly = true)
    public ClubResponseDto findById(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "존재하지 않는 동아리 입니다."
                        )
                );
        return ClubResponseDto.toDto(club);
    }

    // 로그인한 사용자가 가입한 동아리 목록 조회
    @Transactional(readOnly = true)
    public List<ClubResponseDto> findByUser(User user) {
        List<Club> clubs = clubUserRepository.findByUserId(user.getId()).stream().map(ClubUser::getClub).toList();
        return clubs.stream().map(ClubResponseDto::toDto).toList();
    }

    // 동아리 리크루팅 시작
    @Transactional
    public ClubResponseDto recruitingStart(User user, Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "존재하지 않는 동아리 입니다."
                        )
                );
        club.setIsRecruiting(true);
        return ClubResponseDto.toDto(clubRepository.save(club));
    }
}
