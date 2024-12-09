package com.cluting.clutingbackend.clubuser.controller;

import com.cluting.clutingbackend.clubuser.dto.response.ClubUserResponseDto;
import com.cluting.clutingbackend.clubuser.service.ClubUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clubuser")
@RequiredArgsConstructor
public class ClubUserController {
    private final ClubUserService clubUserService;

    @Operation(description = "동아리 ID로 모든 운영진 목록 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "운영진 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "운영진 목록 조회 실패"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/{clubId}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ClubUserResponseDto> findPosts(
            @PathVariable("clubId") Long clubId) {
        return clubUserService.findAll(clubId);
    }
}
