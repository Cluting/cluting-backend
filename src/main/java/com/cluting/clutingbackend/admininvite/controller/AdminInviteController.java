package com.cluting.clutingbackend.admininvite.controller;

import com.cluting.clutingbackend.admininvite.service.AdminInviteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminInviteController {
    private final AdminInviteService adminInviteService;
}
