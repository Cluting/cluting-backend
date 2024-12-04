package com.cluting.clutingbackend.recruit.service;

import com.cluting.clutingbackend.club.domain.Club;
import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.clubuser.repository.ClubUserRepository;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.recruit.domain.RecruitSchedule;
import com.cluting.clutingbackend.recruit.dto.*;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import com.cluting.clutingbackend.recruit.repository.RecruitScheduleRepository;
import com.cluting.clutingbackend.todo.domain.Todo;
import com.cluting.clutingbackend.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruitService {
    private final RecruitRepository recruitRepository;
    private final RecruitScheduleRepository recruitScheduleRepository;
    private final ClubUserRepository clubUserRepository;
    private final TodoRepository todoRepository;

    // [리크루팅 홈] 불러오기
    public RecruitHomeDto getRecruitHome(Long recruitId, Long clubId, Long clubUserId) {
        RecruitClubInfoDto recruitInfo = getRecruitInfo(recruitId);
        RecruitScheduleDto recruitSchedule = getRecruitSchedule(recruitId);
        List<ClubUserInfoDto> adminList = getAdminList(clubId);
        List<TodoDto> userTodos = getUserTodos(clubId, clubUserId);

        return RecruitHomeDto.builder()
                .recruitInfo(recruitInfo)
                .recruitSchedule(recruitSchedule)
                .adminList(adminList)
                .userTodos(userTodos)
                .build();
    }

    // [리크루팅 홈] 동아리 정보 가져오기
    public RecruitClubInfoDto getRecruitInfo(Long recruitId) {
        Recruit recruit = recruitRepository.findRecruitById(recruitId);
        Club club = recruit.getClub();

        return RecruitClubInfoDto.builder()
                .clubName(club.getName())  //동아리 이름
                .clubProfile(club.getProfile())  //동아리 프로필
                .isInterview(recruit.getIsInterview())  //면접까지 진행하는지, 서류까지만 진행하는지 -> 이에 따라 메뉴가 달라짐
                .generation(recruit.getGeneration())  //기수
                .currentStage(recruit.getCurrentStage())  //현재 진행중인 리크루팅 단계
                .build();
    }

    // [리크루팅 홈] 리크루팅 일정 가져오기
    public RecruitScheduleDto getRecruitSchedule(Long recruitId) {
        RecruitSchedule schedule = recruitScheduleRepository.findScheduleByRecruitId(recruitId);
        return RecruitScheduleDto.builder()
                .stage1Start(schedule.getStage1Start()) //리크루팅 준비 기간
                .stage1End(schedule.getStage1End())
                .stage2Start(schedule.getStage2Start()) //공고 업로드
                .stage2End(schedule.getStage2End())
                .stage3Start(schedule.getStage3Start()) //모집기간
                .stage3End(schedule.getStage3End())
                .stage4Start(schedule.getStage4Start()) //서류평가기간
                .stage4End(schedule.getStage4End())
                .stage5Start(schedule.getStage5Start()) //1차 합격자 발표
                .stage5End(schedule.getStage5End())
                .stage6Start(schedule.getStage6Start()) //면접 기간
                .stage6End(schedule.getStage6End())
                .stage7Start(schedule.getStage7Start()) //면접 평가 기간
                .stage7End(schedule.getStage7End())
                .stage8Start(schedule.getStage8Start()) //최종 합격자 발표
                .stage8End(schedule.getStage8End())
                .build();
    }

    // [리크루팅 홈] 운영진 리스트 가져오기
    public List<ClubUserInfoDto> getAdminList(Long clubId) {
        List<ClubUser> staffList = clubUserRepository.findStaffByClubIdAndGeneration(clubId);
        return staffList.stream()
                .map(cu -> ClubUserInfoDto.builder()
                        .name(cu.getUser().getName())
                        .email(cu.getUser().getEmail())
                        .build())
                .collect(Collectors.toList());
    }

    // [리크루팅 홈] 운영진 투두 리스트 가져오기
    public List<TodoDto> getUserTodos(Long clubId, Long clubUserId) {
        ClubUser clubUser = clubUserRepository.findByClubIdAndUserId(clubId, clubUserId);  //해당 운영진
        List<Todo> todos = todoRepository.findTodosByUserId(clubUser.getUser().getId());  //해당 운영진의 투두 리스트
        return todos.stream()
                .map(todo -> TodoDto.builder()
                        .content(todo.getContent())
                        .status(todo.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
