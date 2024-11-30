package com.cluting.clutingbackend.domain.application;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;
}
