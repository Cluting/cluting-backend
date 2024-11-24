package com.cluting.clutingbackend.plan.controller;

import com.cluting.clutingbackend.plan.dto.request.ClubCreateRequestDto;
import com.cluting.clutingbackend.plan.dto.response.ClubResponseDto;
import com.cluting.clutingbackend.plan.service.ClubService;
import com.cluting.clutingbackend.util.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/club")
public class ClubController {
    private final ClubService clubService;

    @Operation(description = "동아리 id로 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "동아리 조회 성공"),
            @ApiResponse(responseCode = "404", description = "Club not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/{clubId}")
    public ClubResponseDto findClub(@PathVariable("clubId") Long clubId) {
        return clubService.findClub(clubId);
    }

    @Operation(description = "로그인 된 사용자가 가입한 동아리 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "동아리 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/userClubs")
    public List<ClubResponseDto> findClubs(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return clubService.userClubs(userDetails.getUser());
    }

    @Operation(description = "가장 인기 있는 동아리 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "동아리 조회 성공"),
            @ApiResponse(responseCode = "404", description = "Club not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/popular")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ClubResponseDto> popular() {
        return clubService.popular();
    }

    @Operation(description = "동아리 추가")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "동아리 추가 성공"),
            @ApiResponse(responseCode = "404", description = "동아리 추가 실패"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PostMapping(value = "/create", consumes = "multipart/form-data")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ClubResponseDto create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute ClubCreateRequestDto clubCreateRequestDto,
            @RequestPart(value = "profile", required = false) MultipartFile profile) {
        return clubService.create(userDetails.getUser(), clubCreateRequestDto, profile);
    }

    @Operation(description = "리크루팅 시작")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "동아리 리크루팅 시작으로 상태 변경 성공"),
            @ApiResponse(responseCode = "404", description = "Club not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PutMapping("/start-recruiting/{clubId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ClubResponseDto startRecruiting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("clubId") Long clubId) {
        return clubService.startRecruiting(userDetails.getUser(), clubId);
    }

}
