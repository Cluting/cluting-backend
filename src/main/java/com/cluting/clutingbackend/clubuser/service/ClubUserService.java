package com.cluting.clutingbackend.clubuser.service;

import com.cluting.clutingbackend.clubuser.repository.ClubUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubUserService {
    private final ClubUserRepository clubUserRepository;
}
