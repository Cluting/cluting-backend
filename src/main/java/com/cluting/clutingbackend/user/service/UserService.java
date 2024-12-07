package com.cluting.clutingbackend.user.service;

import com.cluting.clutingbackend.admininvite.domain.AdminInvite;
import com.cluting.clutingbackend.admininvite.repository.AdminInviteRepository;
import com.cluting.clutingbackend.admininvite.repository.TempUserRepository;
import com.cluting.clutingbackend.club.domain.Club;
import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.clubuser.repository.ClubUserRepository;
import com.cluting.clutingbackend.global.enums.ClubRole;
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

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;
    // 비회원 운영진 초대를 위해 아래 repo 추가
    private final TempUserRepository tempUserRepository;
    private final ClubUserRepository clubUserRepository;
    private final AdminInviteRepository adminInviteRepository;

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

        User user = userRepository.save(
                userSignUpRequestDto.toEntity(passwordEncoder.encode(userSignUpRequestDto.getPassword()))
        );

        // [운영진 초대] 비회원 운영진 초대 시, 회원가입 후 운영진으로 등록되게 하기 위함.
        tempUserRepository.findByEmail(user.getEmail()).ifPresent(tempUser -> {
            AdminInvite adminInvite = tempUser.getInvite();
            Club club = adminInvite.getClub();

            if (!adminInvite.getIsUsed() && adminInvite.getExpirationDate().isAfter(LocalDateTime.now())) {
                ClubUser clubUser = ClubUser.builder()
                        .user(user)
                        .club(club)
                        .role(ClubRole.STAFF)
                        .generation(club.getRecruits().stream()
                                .filter(recruit -> !recruit.getIsDone())
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Active recruitment not found"))
                                .getGeneration()
                        )
                        .build();
                clubUserRepository.save(clubUser);

                // 초대 처리 완료
                adminInvite.setIsUsed(true);
                adminInviteRepository.save(adminInvite);
            }

            // TempUser 삭제
            tempUserRepository.delete(tempUser);
        });


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
