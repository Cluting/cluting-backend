package com.cluting.clutingbackend.plan.service;

import com.cluting.clutingbackend.plan.domain.User;
import com.cluting.clutingbackend.plan.dto.request.UserSignInRequestDto;
import com.cluting.clutingbackend.plan.dto.response.UserSignInResponseDto;
import com.cluting.clutingbackend.plan.dto.request.UserSignUpRequestDto;
import com.cluting.clutingbackend.plan.dto.response.UserResponseDto;
import com.cluting.clutingbackend.plan.repository.UserRepository;
import com.cluting.clutingbackend.util.security.JwtProvider;
import com.cluting.clutingbackend.util.redis.RedisUtil;
import com.cluting.clutingbackend.util.StaticValue;
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
        //TODO 에러핸들링
        userRepository.findByEmail(userSignUpRequestDto.getEmail())
                .ifPresent(user -> {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."
                    );
                });

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

    @Transactional(readOnly = true)
    public UserResponseDto me(User user) {
        return UserResponseDto.toDto(user);
    }
}
