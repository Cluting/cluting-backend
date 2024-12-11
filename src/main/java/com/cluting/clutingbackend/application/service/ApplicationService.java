package com.cluting.clutingbackend.application.service;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.dto.response.ApplicationStatusResponseDto;
import com.cluting.clutingbackend.application.dto.response.RecruitStatus;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.recruit.domain.RecruitSchedule;
import com.cluting.clutingbackend.recruit.repository.RecruitScheduleRepository;
import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final RecruitScheduleRepository recruitScheduleRepository;

    public List<ApplicationStatusResponseDto> getApplicationStatusAndCalendar(CustomUserDetails userDetails) {
        // 현재 유저가 지원한 모든 Application을 가져오기
        List<Application> applicationList = applicationRepository.findByUserId(userDetails.getId());

        // DTO로 변환
        return applicationList.stream()
                .map(app -> {
                    Recruit recruit = app.getRecruit();
                    RecruitSchedule recruitSchedule = recruitScheduleRepository.findByRecruitId(recruit.getId())
                            .orElseThrow(()-> new RuntimeException("RecruitSchedule is Not Found!")); // Schedule 가져오기
                    LocalDate currentDate = LocalDate.now();

                    // 상태 계산
                    RecruitStatus status = determineRecruitStatus(recruitSchedule, currentDate);

                    // DTO 빌드
                    return ApplicationStatusResponseDto.builder()
                            .clubName(recruit.getClub().getName()) // 동아리 이름
                            .clubProfile(recruit.getClub().getProfile()) // 동아리 로고
                            .status(status) // 계산된 상태 설정
                            .recruitmentStartDate(recruitSchedule.getStage3Start())
                            .recruitmentEndDate(recruitSchedule.getStage3End())
                            .documentResultDate(recruitSchedule.getStage4Start()) // 예: 서류 결과 발표일
                            .finalResultDate(recruitSchedule.getStage5Start()) // 예: 최종 결과 발표일
                            .build();
                })
                .collect(Collectors.toList());
    }

    private RecruitStatus determineRecruitStatus(RecruitSchedule schedule, LocalDate currentDate) {
        if (currentDate.isBefore(schedule.getStage4Start())) {
            return RecruitStatus.A; // 지원완료
        } else if (currentDate.isAfter(schedule.getStage4Start()) && currentDate.isBefore(schedule.getStage4End())) {
            return RecruitStatus.B; // 서류 평가 중
        } else if (currentDate.isAfter(schedule.getStage5Start()) && currentDate.isBefore(schedule.getStage5End())) {
            return RecruitStatus.C; // 서류 평가 완료
        } else if (currentDate.isAfter(schedule.getStage6Start()) && currentDate.isBefore(schedule.getStage6End())) {
            return RecruitStatus.D; // 면접 진행 중
        } else{
            return RecruitStatus.E; // 최종합격자발표
        }
    }


}
