package com.cluting.clutingbackend.plan.controller;

import com.cluting.clutingbackend.plan.dto.response.UserResponseDto;
import com.cluting.clutingbackend.plan.dto.request.UserSignInRequestDto;
import com.cluting.clutingbackend.plan.dto.response.UserSignInResponseDto;
import com.cluting.clutingbackend.plan.dto.request.UserSignUpRequestDto;
import com.cluting.clutingbackend.plan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserResponseDto signUp(@RequestBody UserSignUpRequestDto userSignUpRequestDto) {
        return userService.signUp(userSignUpRequestDto);
    }

    @GetMapping("/sign-in")
    @ResponseStatus(value = HttpStatus.OK)
    public UserSignInResponseDto signIn(@RequestBody UserSignInRequestDto userSignInRequestDto) {
        return userService.signIn(userSignInRequestDto);
    }
}
