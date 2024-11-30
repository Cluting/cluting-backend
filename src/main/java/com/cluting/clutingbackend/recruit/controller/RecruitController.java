package com.cluting.clutingbackend.recruit.controller;

import com.cluting.clutingbackend.recruit.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecruitController {
    private final RecruitService recruitService;
}
