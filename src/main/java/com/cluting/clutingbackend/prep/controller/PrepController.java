package com.cluting.clutingbackend.prep.controller;

import com.cluting.clutingbackend.prep.dto.PrepDetailsResponseDto;
import com.cluting.clutingbackend.prep.dto.PrepRequestDto;
import com.cluting.clutingbackend.prep.service.PrepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[1. 계획하기]", description = "계획하기 관련 API")
@RestController
@RequestMapping("/api/v1/prep")
@RequiredArgsConstructor
public class PrepController {
    private final PrepService prepService;

    // [계획하기] 등록하기
    @Operation(
            summary = "[계획하기] 등록하기",
            description = "모집 공고 ID와 리크루팅 일정, 모집 단계별 운영진, 지원자 그룹을 통해 계획하기 페이지를 등록합니다."+
                    "\n\n 그룹이 없다면 null로 보내주세요.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "계획하기 페이지 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "모집 공고를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @PostMapping
    public ResponseEntity<Void> savePreparationDetails(
            @RequestParam Long recruitId,
            @RequestBody PrepRequestDto prepRequestDto
    ) {
        prepService.savePreparation(recruitId, prepRequestDto);
        return ResponseEntity.ok().build();
    }

    // [계획하기] 불러오기
    @Operation(
            summary = "[계획하기] 불러오기",
            description = "모집 공고 ID를 기반으로 리크루팅 일정, 모집 단계별 운영진, 지원자 그룹을 불러옵니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "계획하기 페이지 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "모집 공고를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping
    public PrepDetailsResponseDto getPrepDetails(@RequestParam Long recruitId) {
        return prepService.getPrepDetails(recruitId);
    }
}
