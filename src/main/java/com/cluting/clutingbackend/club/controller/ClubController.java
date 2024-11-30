package com.cluting.clutingbackend.club.controller;

import com.cluting.clutingbackend.club.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;
}
