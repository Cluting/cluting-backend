package com.cluting.clutingbackend.application.service;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.dto.GroupSelectRequestDto;
import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.dto.request.ApplicantProfileRequestDto;
import com.cluting.clutingbackend.application.dto.response.ApplicantProfileResponseDto;
import com.cluting.clutingbackend.application.dto.response.ApplicationStatusResponseDto;
import com.cluting.clutingbackend.application.dto.response.ClubResponseDto;
import com.cluting.clutingbackend.application.dto.response.RecruitStatus;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.global.enums.EvaluateStatus;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.interview.repository.InterviewRepository;
import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.plan.repository.GroupRepository;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.recruit.domain.RecruitSchedule;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import com.cluting.clutingbackend.recruit.repository.RecruitScheduleRepository;
import com.cluting.clutingbackend.user.domain.Scrap;
import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.user.dto.response.UserResponseDto;
import com.cluting.clutingbackend.user.repository.ScrapRepository;
import com.cluting.clutingbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final RecruitRepository recruitRepository;
    private final RecruitScheduleRepository recruitScheduleRepository;
    private final ScrapRepository scrapRepository;
    private final InterviewRepository interviewRepository;

    // [지원서 작성하기] 지원자 정보 조회하기(프로필, 이름, 번호, 이메일, 거주지, 학교, 학과, 다전공)
    @Transactional(readOnly = true)
    public UserResponseDto findUserInfo(User user) {
        return UserResponseDto.toDto(user);
    }

    // [지원서 작성하기] 모집 그룹 목록 조회하기
    @Transactional(readOnly = true)
    public List<String> findGroups(Long recruitId) {
        List<Group> groups = groupRepository.findByRecruitId(recruitId);
        List<String> name = new ArrayList<>();
        for (Group group : groups) {
            name.add(group.getName());
        }
        return name;
    }

    // [지원서 작성하기] 모집 그룹 선택 저장하기 - Application 생성 및 사용자,모집공고,파트 초기 저장
    @Transactional
    public void selectGroup(User user, Long recruitId, GroupSelectRequestDto groupSelectRequestDto) {
        Recruit recruit = recruitRepository.findRecruitById(recruitId);
        applicationRepository.save(
                Application.of(user, recruit, String.join(":::", groupSelectRequestDto.getGroups()))
        );
    }

    // [지원서 작성하기] 공통 질문 조회하기
    @Transactional(readOnly = true)
    public void findCommonQuestion(Long recruitId) {
    }

    // [지원서 작성하기] 공통 질문 답변 저장하기
    // [지원서 작성하기] 파트별(파트가 2개 이상일 때에는 모든 질문) 질문 조회하기
    // [지원서 작성하기] 파트별(파트가 2개 이상일 때에는 모든 질문) 질문 답변 저장하기
    // [지원서 작성하기] 파일 제출일 경우 파일 저장
    // [지원서 작성하기] 지원자의 포트폴리오 url 조회 및 운영진들의 면접 가능 시간 조회
    // [지원서 작성하기] 지원자의 포트폴리오 url 입력 저장 및 운영진들의 면접 가능 시간 기반의 지원자의 면접 가능 시간 선택 저장
    // [지원서 작성하기] 제출 확정하기 - createdAt 저장

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
        List<Scrap> scrappeds = scrapRepository.findAllByUserId(customUserDetails.getId());
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
