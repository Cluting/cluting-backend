package com.cluting.clutingbackend.application.service;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.dto.response.ApplicationStatusResponseDto;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public List<ApplicationStatusResponseDto> getApplicationStatusAndCalendar(CustomUserDetails userDetails) {
        // 현재 유저가 지원한 모든 Application을 가져옵니다.
        List<Application> applicationList = applicationRepository.findByUserId(userDetails.getId());

        // DTO로 변환
        return applications.stream()
                .map(app -> ApplicationStatusResponseDto.builder()
                        .clubName(app.getRecruit().getClub().getName()) // 동아리 이름
                        .clubProfile(app.getRecruit().getClub().getProfile()) // 동아리 로고
                        .status(app.getStatus()) // 지원 상태

                        // 모집 관련 날짜 정보
                        .recruitmentStartDate(app.getRecruit().getRecruitmentStartDate())
                        .recruitmentEndDate(app.getRecruit().getRecruitmentEndDate())
                        .documentResultDate(app.getRecruit().getDocumentResultDate())
                        .finalResultDate(app.getRecruit().getFinalResultDate())
                        .build())
                .collect(Collectors.toList());
    }

}
