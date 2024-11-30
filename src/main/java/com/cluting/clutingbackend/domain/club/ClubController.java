package com.cluting.clutingbackend.domain.club;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;
}
