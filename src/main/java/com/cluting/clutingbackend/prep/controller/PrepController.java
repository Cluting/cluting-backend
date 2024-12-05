package com.cluting.clutingbackend.prep.controller;

import com.cluting.clutingbackend.prep.dto.PrepRequestDto;
import com.cluting.clutingbackend.prep.service.PrepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/prep")
@RequiredArgsConstructor
public class PrepController {
    private final PrepService prepService;

    // [계획하기] 등록하기
    @PostMapping
    public ResponseEntity<Void> savePreparationDetails(
            @RequestParam Long recruitId,
            @RequestBody PrepRequestDto prepRequestDto
    ) {
        prepService.savePreparation(recruitId, prepRequestDto);
        return ResponseEntity.ok().build();
    }
}
