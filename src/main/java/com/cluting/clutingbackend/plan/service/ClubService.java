package com.cluting.clutingbackend.plan.service;

import com.cluting.clutingbackend.plan.domain.Club;
import com.cluting.clutingbackend.plan.domain.User;
import com.cluting.clutingbackend.plan.dto.request.ClubCreateRequestDto;
import com.cluting.clutingbackend.plan.dto.response.ClubResponseDto;
import com.cluting.clutingbackend.plan.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final AwsS3Service awsS3Service;

    // 가장 인기 있는 동아리

    // 동아리 추가
    @Transactional
    public ClubResponseDto create(User user, ClubCreateRequestDto clubCreateRequestDto, MultipartFile profile) {
        // user 검증 로직 필요

        String fileUrl;
        if (profile == null) fileUrl = "https://cluting.s3.ap-northeast-2.amazonaws.com/default.png";
        else fileUrl = awsS3Service.uploadFile(profile);

        Club club = clubRepository.save(
                clubCreateRequestDto.toEntity(fileUrl)
        );
        return ClubResponseDto.toDto(club);
    }

    // 동아리 리크루팅 시작
    @Transactional
    public ClubResponseDto startRecruiting(User user, Long clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "존재하지 않는 동아리입니다."
                )
        );

        club.setIsRecruiting(true);
        return ClubResponseDto.toDto(clubRepository.save(club));
    }

}
