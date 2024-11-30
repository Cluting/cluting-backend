package com.cluting.clutingbackend.domain.clubuser;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClubUserController {
    private final ClubUserService clubUserService;
}
