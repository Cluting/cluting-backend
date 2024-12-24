package com.cluting.clutingbackend.application.service;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.domain.Scrapped;
import com.cluting.clutingbackend.application.dto.request.ApplicantProfileRequestDto;
import com.cluting.clutingbackend.application.dto.response.ApplicantProfileResponseDto;
import com.cluting.clutingbackend.application.dto.response.ApplicationStatusResponseDto;
import com.cluting.clutingbackend.application.dto.response.ClubResponseDto;
import com.cluting.clutingbackend.application.dto.response.RecruitStatus;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.application.repository.InterviewRepository;
import com.cluting.clutingbackend.application.repository.ScrapRepository;
import com.cluting.clutingbackend.global.enums.EvaluateStatus;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.recruit.domain.RecruitSchedule;
import com.cluting.clutingbackend.recruit.repository.RecruitScheduleRepository;
import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final RecruitScheduleRepository recruitScheduleRepository;
    private final ScrapRepository scrapRepository;
    private final InterviewRepository interviewRepository;

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

    public ApplicantProfileResponseDto getInfo(CustomUserDetails userDetails){
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(()-> new RuntimeException("User Not Found!"));

        return ApplicantProfileResponseDto.builder()
                .name(user.getName())
                .phoneNum(user.getPhone())
                .addr(user.getLocation())
                .university(user.getSchool())
                .major(user.getMajor())
                .doubleMajor(user.getDoubleMajor())
                .studentStatus(user.getStudentStatus())
                .semester(user.getSemester())
                .build();

    }

    @Transactional
    public String changeUserInfo(CustomUserDetails userDetails, ApplicantProfileRequestDto dto){
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(()-> new RuntimeException("User Not Found!"));
        user.updateUserInfo(dto);

        return "User's Info is Updated!" + dto.toString();
    }

    public List<ClubResponseDto> getApplyingClubs(CustomUserDetails customUserDetails){
        return applicationRepository.findByUserIdAndRecruitStatus(customUserDetails.getId(),RecruitStatus.Z)
                .stream()
                .map(ClubResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<ClubResponseDto> getAppliedClubs(CustomUserDetails customUserDetails){
        return applicationRepository.findByUserIdAndRecruitStatus(customUserDetails.getId(),RecruitStatus.A)
                .stream()
                .map(ClubResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<ClubResponseDto> getScrapedClubs(CustomUserDetails customUserDetails){
        List<Scrapped> scrappeds = scrapRepository.findAllByUserId(customUserDetails.getId());
        List<Recruit> recruits = new ArrayList<>();

        for (int i=0;i<scrappeds.size();i++){
            recruits.add(scrappeds.get(i).getRecruit());
        }
        return recruits.stream()
                .map(ClubResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<ClubResponseDto> getPassedClubs(Long userId) {
        // 지원서 합격 리스트
        List<ClubResponseDto> passedApplications = applicationRepository.findByUserIdAndState(userId, EvaluateStatus.PASS)
                .stream()
                .map(application -> new ClubResponseDto(application))
                .collect(Collectors.toList());

        // 면접 합격 리스트
        List<ClubResponseDto> passedInterviews = interviewRepository.findByApplication_UserIdAndState(userId, EvaluateStatus.PASS)
                .stream()
                .map(interview -> new ClubResponseDto(interview.getApplication()))
                .collect(Collectors.toList());

        // 두 리스트를 합치기
        passedApplications.addAll(passedInterviews);
        return passedApplications;
    }

    public List<ClubResponseDto> getFailedClubs(Long userId) {
        // 지원서 불합격 리스트
        List<ClubResponseDto> failedApplications = applicationRepository.findByUserIdAndState(userId, EvaluateStatus.FAIL)
                .stream()
                .map(application -> new ClubResponseDto(application))
                .collect(Collectors.toList());

        // 면접 불합격 리스트
        List<ClubResponseDto> failedInterviews = interviewRepository.findByApplication_UserIdAndState(userId, EvaluateStatus.FAIL)
                .stream()
                .map(interview -> new ClubResponseDto(interview.getApplication()))
                .collect(Collectors.toList());

        // 두 리스트를 합치기
        failedApplications.addAll(failedInterviews);
        return failedApplications;
    }



}
