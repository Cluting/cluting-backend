package com.cluting.clutingbackend.user.service;

import com.cluting.clutingbackend.admininvite.domain.AdminInvite;
import com.cluting.clutingbackend.admininvite.repository.AdminInviteRepository;
import com.cluting.clutingbackend.admininvite.repository.TempUserRepository;
import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.club.domain.Club;
import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.clubuser.repository.ClubUserRepository;
import com.cluting.clutingbackend.global.enums.ClubRole;
import com.cluting.clutingbackend.global.enums.Status;
import com.cluting.clutingbackend.global.s3.AwsS3Service;
import com.cluting.clutingbackend.recruit.domain.RecruitSchedule;
import com.cluting.clutingbackend.recruit.repository.RecruitScheduleRepository;
import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.user.dto.request.*;
import com.cluting.clutingbackend.user.dto.response.UserApplicatedClubResponseDto;
import com.cluting.clutingbackend.user.dto.response.UserResponseDto;
import com.cluting.clutingbackend.user.dto.response.UserSignInResponseDto;
import com.cluting.clutingbackend.global.security.JwtProvider;
import com.cluting.clutingbackend.global.util.RedisUtil;
import com.cluting.clutingbackend.global.util.StaticValue;
import com.cluting.clutingbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;
    private final AwsS3Service awsS3Service;
    // 비회원 운영진 초대를 위해 아래 repo 추가
    private final TempUserRepository tempUserRepository;
    private final ClubUserRepository clubUserRepository;
    private final AdminInviteRepository adminInviteRepository;
    private final ApplicationRepository applicationRepository;
    private final RecruitScheduleRepository recruitScheduleRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deletePersonalInfo() {
        LocalDateTime dueDate = LocalDateTime.now().minusYears(3);

        userRepository.findAllByStatus(Status.WITHDRAW).stream()
                .filter(user -> user.getUpdatedAt().isBefore(dueDate))
                .forEach(user -> {
                    try {
                        user.encrypt();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    userRepository.save(user);
                });
    }

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

    @Transactional(readOnly = true)
    public List<UserApplicatedClubResponseDto> profileHome(User user) {
        List<UserApplicatedClubResponseDto> result = new ArrayList<>();
        List<Application> applications = applicationRepository.findAllByUserId(user.getId());
        LocalDate now = LocalDate.now();

        for (Application application : applications) {
            Long recruitId = application.getRecruit().getId();
            RecruitSchedule recruitSchedule = recruitScheduleRepository.findByRecruitId(recruitId).orElseThrow(
                    () -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "존재하지 않는 모집공고 입니다."
                    )
            );

            String currentStage;
            if (isDateInRange(now, recruitSchedule.getStage3Start(), recruitSchedule.getStage3End())) {
                currentStage = "지원 완료";
            } else if (isDateInRange(now, recruitSchedule.getStage4Start(), recruitSchedule.getStage4End())) {
                currentStage = "서류 평가 중";
            } else if (isDateInRange(now, recruitSchedule.getStage5Start(), recruitSchedule.getStage5End())) {
                currentStage = "서류 합격자 발표";
            } else if (isDateInRange(now, recruitSchedule.getStage6Start(), recruitSchedule.getStage7End())) {
                currentStage = "면접 평가 중";
            } else if (isDateInRange(now, recruitSchedule.getStage8Start(), recruitSchedule.getStage8End())) {
                currentStage = "최종 합격자 발표";
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "모집 일정이 존재하지 않습니다.");
            }

            UserApplicatedClubResponseDto userApplicatedClubResponseDto = UserApplicatedClubResponseDto.builder()
                    .clubId(application.getRecruit().getClub().getId())
                    .clubName(application.getRecruit().getClub().getName())
                    .clubProfile(application.getRecruit().getClub().getProfile())
                    .recruitId(recruitId)
                    .currentStage(currentStage)
                    .stage1Start(recruitSchedule.getStage1Start())
                    .stage1End(recruitSchedule.getStage1End())
                    .stage2Start(recruitSchedule.getStage2Start())
                    .stage2End(recruitSchedule.getStage2End())
                    .stage3Start(recruitSchedule.getStage3Start())
                    .stage3End(recruitSchedule.getStage3End())
                    .stage4Start(recruitSchedule.getStage4Start())
                    .stage4End(recruitSchedule.getStage4End())
                    .stage5Start(recruitSchedule.getStage5Start())
                    .stage5End(recruitSchedule.getStage5End())
                    .stage6Start(recruitSchedule.getStage6Start())
                    .stage6End(recruitSchedule.getStage6End())
                    .stage7Start(recruitSchedule.getStage7Start())
                    .stage7End(recruitSchedule.getStage7End())
                    .stage8Start(recruitSchedule.getStage8Start())
                    .stage8End(recruitSchedule.getStage8End())
                    .build();
            result.add(userApplicatedClubResponseDto);
        }

        return result;
    }

    private boolean isDateInRange(LocalDate now, LocalDate start, LocalDate end) {
        return (start != null && end != null) && (now.isEqual(start) || now.isEqual(end) || (now.isAfter(start) && now.isBefore(end)));
    }

    @Transactional
    public UserResponseDto update(User user, UserUpdateRequestDto userUpdateRequestDto) {
        user.update(
                userUpdateRequestDto.getName(),
                userUpdateRequestDto.getPhone(),
                userUpdateRequestDto.getLocation(),
                userUpdateRequestDto.getSchool(),
                userUpdateRequestDto.getMajor(),
                userUpdateRequestDto.getDoubleMajor(),
                userUpdateRequestDto.getStudentStatus(),
                userUpdateRequestDto.getSemester()
        );
        return UserResponseDto.toDto(userRepository.save(user));
    }

    @Transactional
    public UserResponseDto update(User user, MultipartFile profile) {
        user.update(awsS3Service.uploadFile(profile));
        return UserResponseDto.toDto(userRepository.save(user));
    }

    @Transactional
    public UserResponseDto updatePortfolioFile(User user, MultipartFile portfolio) {
        user.setPortfolioFile(awsS3Service.uploadFile(portfolio));
        return UserResponseDto.toDto(userRepository.save(user));
    }

    @Transactional
    public UserResponseDto updatePortfolioUrl(User user, String portfolioUrl) {
        user.setPortfolioUrl(portfolioUrl);
        return UserResponseDto.toDto(userRepository.save(user));
    }
}
