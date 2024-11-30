package com.cluting.clutingbackend.domain.clubuser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubUserService {
    private final ClubUserRepository clubUserRepository;
}
