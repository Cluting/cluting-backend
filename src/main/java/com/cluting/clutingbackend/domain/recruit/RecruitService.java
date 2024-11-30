package com.cluting.clutingbackend.domain.recruit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitService {
    private final RecruitRepository recruitRepository;
}
