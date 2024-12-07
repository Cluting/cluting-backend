package com.cluting.clutingbackend.club.controller;

import com.cluting.clutingbackend.club.dto.request.ClubRegisterRequestDto;
import com.cluting.clutingbackend.club.dto.response.ClubResponseDto;
import com.cluting.clutingbackend.club.service.ClubService;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/club")
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;

    @Operation(description = "동아리 등록 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "동아리 추가 성공"),
            @ApiResponse(responseCode = "404", description = "동아리 추가 실패"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ClubResponseDto register(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute ClubRegisterRequestDto clubCreateRequestDto,
            @RequestPart(value = "profile", required = false) MultipartFile profile) {
        return clubService.registerClub(userDetails.getUser(), clubCreateRequestDto, profile);
    }

    // 홈페이지
    @Operation(description = "가장 인기있는 동아리 3개 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "동아리 조회 성공"),
            @ApiResponse(responseCode = "404", description = "동아리 조회 실패"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/popular")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ClubResponseDto> popular(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return clubService.popular(userDetails.getUser());
    }

    @Operation(description = "ID로 동아리 단일 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "동아리 조회 성공"),
            @ApiResponse(responseCode = "404", description = "동아리 조회 실패"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/{clubId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ClubResponseDto findById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("clubId") Long clubId) {
        return clubService.findById(clubId);
    }

    @Operation(description = "로그인 된 사용자가 가입한 동아리 목록 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "동아리 조회 성공"),
            @ApiResponse(responseCode = "404", description = "동아리 조회 실패"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/user")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ClubResponseDto> findByUser(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return clubService.findByUser(userDetails.getUser());
    }

    @Operation(description = "동아리 리크루팅 시작 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "동아리 리크루팅 시작 성공"),
            @ApiResponse(responseCode = "404", description = "동아리 리크루팅 시작 실패"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PutMapping("/start/{clubId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ClubResponseDto recruitingStart(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("clubId") Long clubId) {
        return clubService.recruitingStart(userDetails.getUser(), clubId);
    }
}
