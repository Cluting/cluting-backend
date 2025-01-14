package com.cluting.clutingbackend.prep.service;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.clubuser.repository.ClubUserRepository;
import com.cluting.clutingbackend.global.enums.CurrentStage;
import com.cluting.clutingbackend.interview.domain.InterviewEvaluator;
import com.cluting.clutingbackend.interview.repository.InterviewEvaluatorRepository;
import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.plan.repository.*;
import com.cluting.clutingbackend.prep.domain.PrepStage;
import com.cluting.clutingbackend.prep.domain.PrepStageClubUser;
import com.cluting.clutingbackend.prep.dto.PrepDetailsResponseDto;
import com.cluting.clutingbackend.prep.dto.PrepRequestDto;
import com.cluting.clutingbackend.prep.dto.PrepStageDto;
import com.cluting.clutingbackend.prep.dto.PrepStageResponseDto;
import com.cluting.clutingbackend.prep.repository.PrepStageClubUserRepository;
import com.cluting.clutingbackend.prep.repository.PrepStageRepository;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.recruit.domain.RecruitSchedule;
import com.cluting.clutingbackend.recruit.dto.RecruitScheduleDto;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import com.cluting.clutingbackend.recruit.repository.RecruitScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PrepService {
    private final RecruitScheduleRepository recruitScheduleRepository;
    private final RecruitRepository recruitRepository;
    private final PrepStageRepository prepStageRepository;
    private final PrepStageClubUserRepository prepStageClubUserRepository;
    private final ClubUserRepository clubUserRepository;
    private final GroupRepository groupRepository;
    private final IdealRepository idealRepository;
    private final DocumentCriteriaRepository documentCriteriaRepository;
    private final DocumentEvaluatorRepository documentEvaluatorRepository;
    private final DocumentQuestionRepository documentQuestionRepository;
    private final InterviewEvaluatorRepository interviewEvaluatorRepository;

    // [계획하기] 설정 완료하기
    @Transactional
    public void savePreparation(Long recruitId, PrepRequestDto prepRequestDto) {
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모집 공고를 찾을 수 없습니다. id: " + recruitId));

        // 1. 리크루팅 일정 저장
        RecruitSchedule recruitSchedule = new RecruitSchedule();
        recruitSchedule.setRecruit(recruit);

        RecruitScheduleDto scheduleDto = prepRequestDto.getRecruitSchedules().get(0);
        mapSchedule(recruitSchedule, scheduleDto);

        recruitScheduleRepository.save(recruitSchedule);

        // 2. 모집 단계 및 운영진 저장
        for (PrepStageDto stageDto : prepRequestDto.getPrepStages()) {
            PrepStage prepStage = PrepStage.builder()
                    .recruit(recruit)
                    .stageName(stageDto.getStageName())
                    .stageOrder(stageDto.getStageOrder())
                    .build();
            prepStageRepository.save(prepStage);

            for (Long clubUserId : stageDto.getClubUserIds()) {
                ClubUser clubUser = clubUserRepository.findById(clubUserId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 ClubUser 찾지 못함. id: " + clubUserId));

                PrepStageClubUser prepStageClubUser = PrepStageClubUser.builder()
                        .prepStage(prepStage)
                        .clubUser(clubUser)
                        .build();
                prepStageClubUserRepository.save(prepStageClubUser);
            }
        }

        // 3. 지원자 그룹 저장
        saveApplicantGroups(recruit, prepRequestDto.getApplicantGroups());

        // 4. 현재 진행 중인 리크루팅 단계 PLAN으로 변경
        recruit.setCurrentStage(CurrentStage.PLAN);
        recruitRepository.save(recruit);
    }

    private void mapSchedule(RecruitSchedule recruitSchedule, RecruitScheduleDto scheduleDto) {
        recruitSchedule.setStage1Start(scheduleDto.getStage1Start());
        recruitSchedule.setStage1End(scheduleDto.getStage1End());
        recruitSchedule.setStage2Start(scheduleDto.getStage2Start());
        recruitSchedule.setStage2End(scheduleDto.getStage2End());
        recruitSchedule.setStage3Start(scheduleDto.getStage3Start());
        recruitSchedule.setStage3End(scheduleDto.getStage3End());
        recruitSchedule.setStage4Start(scheduleDto.getStage4Start());
        recruitSchedule.setStage4End(scheduleDto.getStage4End());
        recruitSchedule.setStage5Start(scheduleDto.getStage5Start());
        recruitSchedule.setStage5End(scheduleDto.getStage5End());
        recruitSchedule.setStage6Start(scheduleDto.getStage6Start());
        recruitSchedule.setStage6End(scheduleDto.getStage6End());
        recruitSchedule.setStage7Start(scheduleDto.getStage7Start());
        recruitSchedule.setStage7End(scheduleDto.getStage7End());
        recruitSchedule.setStage8Start(scheduleDto.getStage8Start());
        recruitSchedule.setStage8End(scheduleDto.getStage8End());
    }

    private void saveApplicantGroups(Recruit recruit, List<String> applicantGroups) {
        if (applicantGroups == null || applicantGroups.isEmpty() || applicantGroups.get(0) == null) {
            applicantGroups = List.of("공통");
        }

        for (String groupName : applicantGroups) {
            Group group = new Group();
            group.setRecruit(recruit);
            group.setName(groupName);
            if ("공통".equals(groupName)) {
                group.setCommon(true);
            }
            groupRepository.save(group);
        }
    }

    @Transactional
    public void updatePreparation(Long recruitId, PrepRequestDto prepRequestDto) {
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모집 공고를 찾을 수 없습니다. id: " + recruitId));

        // 1. 리크루팅 일정 수정
        RecruitSchedule recruitSchedule = recruitScheduleRepository.findByRecruitId(recruitId)
                .orElseThrow(() -> new IllegalArgumentException("리크루팅 일정이 존재하지 않습니다. id: " + recruitId));

        RecruitScheduleDto scheduleDto = prepRequestDto.getRecruitSchedules().get(0);
        mapSchedule(recruitSchedule, scheduleDto);
        recruitScheduleRepository.save(recruitSchedule);

        // 2. 모집 단계 및 운영진 수정
        List<PrepStage> existingStages = prepStageRepository.findByRecruitId(recruitId);

        // 삭제할 단계 찾기
        for (PrepStage stage : existingStages) {
            boolean existsInRequest = prepRequestDto.getPrepStages().stream()
                    .anyMatch(dto -> dto.getStageOrder().equals(stage.getStageOrder()));
            if (!existsInRequest) {
                prepStageClubUserRepository.deleteAllByPrepStageId(stage.getId());
                prepStageRepository.delete(stage);
            }
        }

        // 추가 및 수정
        for (PrepStageDto stageDto : prepRequestDto.getPrepStages()) {
            PrepStage prepStage = prepStageRepository.findByRecruitIdAndStageOrder(recruitId, stageDto.getStageOrder())
                    .orElse(PrepStage.builder().build());

            prepStage.setRecruit(recruit);
            prepStage.setStageName(stageDto.getStageName());
            prepStage.setStageOrder(stageDto.getStageOrder());
            prepStageRepository.save(prepStage);

            updateClubUsers(prepStage, stageDto.getClubUserIds());
        }

        // 3. 지원자 그룹 수정
        updateApplicantGroups(recruit, prepRequestDto.getApplicantGroups());
    }


    private void updateClubUsers(PrepStage prepStage, List<Long> clubUserIds) {
        List<PrepStageClubUser> existingUsers = prepStageClubUserRepository.findAllByPrepStageId(prepStage.getId());

        // 삭제
        for (PrepStageClubUser user : existingUsers) {
            if (!clubUserIds.contains(user.getClubUser().getId())) {
                prepStageClubUserRepository.delete(user);
            }
        }

        // 추가
        for (Long clubUserId : clubUserIds) {
            boolean exists = existingUsers.stream()
                    .anyMatch(user -> user.getClubUser().getId().equals(clubUserId));
            if (!exists) {
                ClubUser clubUser = clubUserRepository.findById(clubUserId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 ClubUser 찾지 못함. id: " + clubUserId));

                PrepStageClubUser newUser = PrepStageClubUser.builder()
                        .prepStage(prepStage)
                        .clubUser(clubUser)
                        .build();
                prepStageClubUserRepository.save(newUser);
            }
        }
    }

    private void updateApplicantGroups(Recruit recruit, List<String> applicantGroups) {
        List<Group> existingGroups = groupRepository.findByRecruitId(recruit.getId());
        List<String> existingNames = existingGroups.stream()
                .map(Group::getName)
                .collect(Collectors.toList());

        // 삭제
        for (Group group : existingGroups) {
            if (!applicantGroups.contains(group.getName())) {
                groupRepository.delete(group);
            }
        }

        // 추가
        for (String groupName : applicantGroups) {
            if (!existingNames.contains(groupName)) {
                Group group = new Group();
                group.setRecruit(recruit);
                group.setName(groupName);
                if ("공통".equals(groupName)) {
                    group.setCommon(true);
                }
                groupRepository.save(group);
            }
        }
    }


    // [계획하기] 불러오기
    public PrepDetailsResponseDto getPrepDetails(Long recruitId) {
        // 리크루팅 일정 가져오기
        RecruitSchedule schedule = recruitScheduleRepository.findByRecruitId(recruitId)
                .orElse(null);

        RecruitScheduleDto scheduleDto = schedule != null ? RecruitScheduleDto.builder()
                .stage1Start(schedule.getStage1Start())
                .stage1End(schedule.getStage1End())
                .stage2Start(schedule.getStage2Start())
                .stage2End(schedule.getStage2End())
                .stage3Start(schedule.getStage3Start())
                .stage3End(schedule.getStage3End())
                .stage4Start(schedule.getStage4Start())
                .stage4End(schedule.getStage4End())
                .stage5Start(schedule.getStage5Start())
                .stage5End(schedule.getStage5End())
                .stage6Start(schedule.getStage6Start())
                .stage6End(schedule.getStage6End())
                .stage7Start(schedule.getStage7Start())
                .stage7End(schedule.getStage7End())
                .stage8Start(schedule.getStage8Start())
                .stage8End(schedule.getStage8End())
                .build() : null;

        // 모집준비단계별 운영진 가져오기
        List<PrepStageResponseDto> prepStages = prepStageRepository.findByRecruitId(recruitId).stream()
                .map(prepStage -> {
                    List<String> adminNames = prepStageClubUserRepository.findByPrepStageId(prepStage.getId()).stream()
                            .map(prepStageClubUser -> prepStageClubUser.getClubUser().getUser().getName())
                            .collect(Collectors.toList());
                    return new PrepStageResponseDto(prepStage.getStageName(), adminNames);
                }).collect(Collectors.toList());

        // 지원자 그룹 가져오기
        List<String> groups = groupRepository.findByRecruitId(recruitId).stream()
                .map(Group::getName)
                .collect(Collectors.toList());

        // 운영진 리스트 가져오기
        List<PrepDetailsResponseDto.AdminInfoDto> adminList = clubUserRepository.findStaffNamesByRecruitId(recruitId).stream()
                .map(result -> new PrepDetailsResponseDto.AdminInfoDto(
                        ((Number) result[0]).longValue(), // 첫 번째 값: Long ID
                        (String) result[1]               // 두 번째 값: String Name
                ))
                .collect(Collectors.toList());

        return new PrepDetailsResponseDto(scheduleDto, prepStages, groups, adminList);
    }

}
