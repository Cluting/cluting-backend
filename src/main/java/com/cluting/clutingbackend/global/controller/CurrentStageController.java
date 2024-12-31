package com.cluting.clutingbackend.global.controller;

import com.cluting.clutingbackend.global.enums.CurrentStage;
import com.cluting.clutingbackend.global.service.CurrentStageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "전역적으로 제공해야 하는 API 모음")
@RestController
@RequestMapping("/api/v1/global")
@RequiredArgsConstructor
public class CurrentStageController {

    private final CurrentStageService currentStageService;

    @Operation(
            summary = "[리크루팅 공고 진행 단계] 현재 단계(전/중/후) 불러오기"
    )
    @GetMapping("/current-stage")
    public CurrentStage getCurrentStage(@RequestParam Long recruitId){
        return currentStageService.getCurrentStage(recruitId);
    }
}
