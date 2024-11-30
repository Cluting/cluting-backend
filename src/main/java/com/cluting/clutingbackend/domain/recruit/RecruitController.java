package com.cluting.clutingbackend.domain.recruit;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecruitController {
    private final RecruitService recruitService;
}
