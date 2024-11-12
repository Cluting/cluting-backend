package com.cluting.clutingbackend.user.controller;

import com.cluting.clutingbackend.user.dto.UserResponseDto;
import com.cluting.clutingbackend.user.dto.UserSignInRequestDto;
import com.cluting.clutingbackend.user.dto.UserSignInResponseDto;
import com.cluting.clutingbackend.user.dto.UserSignUpRequestDto;
import com.cluting.clutingbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
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
