package com.cluting.clutingbackend.user.service;

import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.user.dto.response.UserResponseDto;
import com.cluting.clutingbackend.user.dto.request.UserSignInRequestDto;
import com.cluting.clutingbackend.user.dto.response.UserSignInResponseDto;
import com.cluting.clutingbackend.user.dto.request.UserSignUpRequestDto;
import com.cluting.clutingbackend.global.security.JwtProvider;
import com.cluting.clutingbackend.global.util.RedisUtil;
import com.cluting.clutingbackend.global.util.StaticValue;
import com.cluting.clutingbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;

    @Transactional
    public UserResponseDto signUp(UserSignUpRequestDto userSignUpRequestDto) {

        if (!userSignUpRequestDto.getTermsOfService() || !userSignUpRequestDto.getPrivacyPolicy()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "필수 동의 사항입니다."
            );
        }

        userRepository.findByEmail(userSignUpRequestDto.getEmail())
                .ifPresent(user -> {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."
                    );
                });

        userSignUpRequestDto.setPhone(userSignUpRequestDto.getPhone().replaceAll("-", ""));
        User user = userRepository.save(
                userSignUpRequestDto.toEntity(passwordEncoder.encode(userSignUpRequestDto.getPassword()))
        );
        return UserResponseDto.toDto(user);
    }

    @Transactional
    public UserSignInResponseDto signIn(UserSignInRequestDto userSignInRequestDto) {
        User user = userRepository.findByEmail(userSignInRequestDto.getEmail()).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "존재하지 않는 이메일입니다."
                )
        );

        String refreshToken = jwtProvider.createRefreshToken();
        redisUtil.setRefreshTokenData(refreshToken, user.getEmail(), StaticValue.JWT_REFRESH_TOKEN_VALID_TIME);

        return new UserSignInResponseDto(
                jwtProvider.createAccessToken(user.getEmail()),
                jwtProvider.createRefreshToken()
        );
    }

    @Transactional
    public UserResponseDto me(User user) {
        return UserResponseDto.toDto(user);
    }
}
