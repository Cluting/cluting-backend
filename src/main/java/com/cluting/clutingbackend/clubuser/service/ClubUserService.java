package com.cluting.clutingbackend.clubuser.service;

import com.cluting.clutingbackend.clubuser.dto.response.ClubUserResponseDto;
import com.cluting.clutingbackend.clubuser.repository.ClubUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubUserService {
    private final ClubUserRepository clubUserRepository;

    @Transactional(readOnly = true)
    public List<ClubUserResponseDto> findAll(Long clubId) {
        return clubUserRepository.findByClub_Id(clubId).stream().map(ClubUserResponseDto::toDto).toList();
    }
}
