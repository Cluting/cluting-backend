package com.cluting.clutingbackend.user.controller;

import com.cluting.clutingbackend.user.dto.request.*;
import com.cluting.clutingbackend.user.dto.response.UserApplicatedClubResponseDto;
import com.cluting.clutingbackend.user.service.UserService;
import com.cluting.clutingbackend.user.dto.response.UserResponseDto;
import com.cluting.clutingbackend.user.dto.response.UserSignInResponseDto;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @Operation(description = "회원가입")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "404", description = "이미 존재하는 이메일 입니다."),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PostMapping("/sign-up")
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserResponseDto signUp(@RequestBody UserSignUpRequestDto userSignUpRequestDto) {
        return userService.signUp(userSignUpRequestDto);
    }

    @Operation(description = "로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 입니다."),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PostMapping("/sign-in")
    @ResponseStatus(value = HttpStatus.OK)
    public UserSignInResponseDto signIn(@RequestBody UserSignInRequestDto userSignInRequestDto) {
        return userService.signIn(userSignInRequestDto);
    }

    @Operation(description = "로그인 한 사용자의 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 입니다."),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/me")
    public UserResponseDto me(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.me(userDetails.getUser());
    }

    @Operation(description = "지원자 마이페이지 홈 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "지원자 마이페이지 홈 조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 입니다."),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/home")
    public List<UserApplicatedClubResponseDto> profileHome(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.profileHome(userDetails.getUser());
    }

    @Operation(description = "사용자 정보 업데이트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 정보 업데이트 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 입니다."),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PutMapping("/home/update")
    public UserResponseDto update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return userService.update(userDetails.getUser(), userUpdateRequestDto);
    }

    @Operation(description = "사용자 프로필 이미지 업데이트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 프로필 이미지 업데이트 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 입니다."),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PutMapping("/home/profile/update")
    public UserResponseDto update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart UserProfileSaveRequestDto userProfileSaveRequestDto) {
        return userService.update(userDetails.getUser(), userProfileSaveRequestDto);
    }

    @Operation(description = "사용자 포트폴리오 url 업데이트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 포트폴리오 url 업데이트 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 입니다."),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PutMapping("/home/portfolio/url")
    public UserResponseDto update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("url") String url) {
        return userService.updatePortfolioUrl(userDetails.getUser(), url);
    }

    @Operation(description = "사용자 포트폴리오 파일 업데이트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 포트폴리오 파일 업데이트 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 입니다."),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PutMapping("/home/portfolio/file")
    public UserResponseDto update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart UserPortfolioUpdateRequestDto userPortfolioUpdateRequestDto) {
        return userService.updatePortfolioFile(userDetails.getUser(), userPortfolioUpdateRequestDto);
    }
}
