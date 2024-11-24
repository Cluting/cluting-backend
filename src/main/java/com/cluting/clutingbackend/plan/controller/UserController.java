package com.cluting.clutingbackend.plan.controller;

import com.cluting.clutingbackend.plan.dto.response.UserResponseDto;
import com.cluting.clutingbackend.plan.dto.request.UserSignInRequestDto;
import com.cluting.clutingbackend.plan.dto.response.UserSignInResponseDto;
import com.cluting.clutingbackend.plan.dto.request.UserSignUpRequestDto;
import com.cluting.clutingbackend.plan.service.UserService;
import com.cluting.clutingbackend.util.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/sign-in")
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
}
