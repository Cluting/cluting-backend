package com.cluting.clutingbackend.domain.club;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
}
