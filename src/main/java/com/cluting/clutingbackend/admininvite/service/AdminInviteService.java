package com.cluting.clutingbackend.admininvite.service;

import com.cluting.clutingbackend.admininvite.repository.AdminInviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminInviteService {
    private final AdminInviteRepository adminInviteRepository;
}
