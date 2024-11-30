package com.cluting.clutingbackend.clubuser.controller;

import com.cluting.clutingbackend.clubuser.service.ClubUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClubUserController {
    private final ClubUserService clubUserService;
}
