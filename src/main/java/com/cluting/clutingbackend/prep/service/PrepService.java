package com.cluting.clutingbackend.prep.service;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.clubuser.repository.ClubUserRepository;
import com.cluting.clutingbackend.global.enums.CurrentStage;
import com.cluting.clutingbackend.interview.repository.InterviewEvaluatorRepository;
import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.plan.repository.*;
import com.cluting.clutingbackend.prep.domain.PrepStage;
import com.cluting.clutingbackend.prep.domain.PrepStageClubUser;
import com.cluting.clutingbackend.prep.dto.PrepDetailsDto;
import com.cluting.clutingbackend.prep.dto.PrepRequestDto;
import com.cluting.clutingbackend.prep.dto.PrepStageDto;
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
    public void savePreparation(Long recruitId, PrepDetailsDto prepDetailsDto) {
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모집 공고를 찾을 수 없습니다. id: " + recruitId));

        // 1. 리크루팅 일정 저장
        RecruitSchedule recruitSchedule = new RecruitSchedule();
        recruitSchedule.setRecruit(recruit);

        RecruitScheduleDto scheduleDto = prepDetailsDto.getSchedule();
        mapSchedule(recruitSchedule, scheduleDto);

        recruitScheduleRepository.save(recruitSchedule);

        // 2. 모집 단계 및 운영진 저장
        for (PrepStageDto stageDto : prepDetailsDto.getPrepStages()) {
            PrepStage prepStage = PrepStage.builder()
                    .recruit(recruit)
                    .stageName(stageDto.getStageName())
                    .stageOrder(stageDto.getStageOrder())
                    .build();
            prepStageRepository.save(prepStage);

            for (Long clubUserId : stageDto.getAdmins().stream().map(PrepStageDto.AdminInfoDto::getId).collect(Collectors.toList())) {
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
        saveApplicantGroups(recruit, prepDetailsDto.getGroups());

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
    public void updatePreparation(Long recruitId, PrepDetailsDto prepDetailsDto) {

        // 1. 모집 공고 유효성 검증
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모집 공고를 찾을 수 없습니다. id: " + recruitId));

        // 2. 리크루팅 일정 수정
        if (prepDetailsDto.getSchedule() != null) {
            updateRecruitSchedule(recruitId, prepDetailsDto.getSchedule());
        }

        // 3. 모집 단계 수정
        if (prepDetailsDto.getPrepStages() != null) {
            updatePrepStages(recruit, prepDetailsDto.getPrepStages());
        }

        // 4. 지원자 그룹 수정
        if (prepDetailsDto.getGroups() != null) {
            updateApplicantGroups(recruit, prepDetailsDto.getGroups());
        }



//        Recruit recruit = recruitRepository.findById(recruitId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 모집 공고를 찾을 수 없습니다. id: " + recruitId));
//
//        // 1. 리크루팅 일정 수정
//        RecruitSchedule recruitSchedule = recruitScheduleRepository.findByRecruitId(recruitId)
//                .orElseThrow(() -> new IllegalArgumentException("리크루팅 일정이 존재하지 않습니다. id: " + recruitId));
//
//        RecruitScheduleDto scheduleDto = prepDetailsDto.getSchedule();
//        mapSchedule(recruitSchedule, scheduleDto);
//        recruitScheduleRepository.save(recruitSchedule);
//
//        // 2. 모집 단계 및 운영진 수정
//        List<PrepStage> existingStages = prepStageRepository.findByRecruitId(recruitId);
//
//        // 삭제할 단계 찾기
//        for (PrepStage stage : existingStages) {
//            boolean existsInRequest = prepDetailsDto.getPrepStages().stream()
//                    .anyMatch(dto -> dto.getStageOrder().equals(stage.getStageOrder()));
//            if (!existsInRequest) {
//                prepStageClubUserRepository.deleteAllByPrepStageId(stage.getId());
//                prepStageRepository.delete(stage);
//            }
//        }
//
//        // 추가 및 수정
//        for (PrepStageDto stageDto : prepDetailsDto.getPrepStages()) {
//            PrepStage prepStage = prepStageRepository.findByRecruitIdAndStageOrder(recruitId, stageDto.getStageOrder())
//                    .orElse(PrepStage.builder().build());
//
//            prepStage.setRecruit(recruit);
//            prepStage.setStageName(stageDto.getStageName());
//            prepStage.setStageOrder(stageDto.getStageOrder());
//            prepStageRepository.save(prepStage);
//
//            updateClubUsers(prepStage, stageDto.getAdmins().stream().map(PrepStageDto.AdminInfoDto::getId).collect(Collectors.toList()));
//        }
//        // 3. 지원자 그룹 수정
//        updateApplicantGroups(recruit, prepDetailsDto.getGroups());
    }

    private void updateRecruitSchedule(Long recruitId, RecruitScheduleDto scheduleDto) {
        RecruitSchedule recruitSchedule = recruitScheduleRepository.findByRecruitId(recruitId)
                .orElseThrow(() -> new IllegalArgumentException("리크루팅 일정이 존재하지 않습니다. id: " + recruitId));

        mapSchedule(recruitSchedule, scheduleDto); // DTO 데이터를 엔티티로 매핑
        recruitScheduleRepository.save(recruitSchedule);
    }
    private void updatePrepStages(Recruit recruit, List<PrepStageDto> prepStages) {
        List<PrepStage> existingStages = prepStageRepository.findByRecruitId(recruit.getId());

        // 삭제 단계 처리
        for (PrepStage stage : existingStages) {
            boolean existsInRequest = prepStages.stream()
                    .anyMatch(dto -> dto.getStageOrder().equals(stage.getStageOrder()));
            if (!existsInRequest) {
                prepStageClubUserRepository.deleteAllByPrepStageId(stage.getId());
                prepStageRepository.delete(stage);
            }
        }

        // 추가 및 수정 처리
        for (PrepStageDto stageDto : prepStages) {
            PrepStage prepStage = prepStageRepository.findByRecruitIdAndStageOrder(recruit.getId(), stageDto.getStageOrder())
                    .orElse(PrepStage.builder().build());

            prepStage.setRecruit(recruit);
            prepStage.setStageName(stageDto.getStageName());
            prepStage.setStageOrder(stageDto.getStageOrder());
            prepStageRepository.save(prepStage);

            List<Long> adminIds = stageDto.getAdmins().stream()
                    .map(PrepStageDto.AdminInfoDto::getId)
                    .collect(Collectors.toList());
            updateClubUsers(prepStage, adminIds);
        }
    }
//    private void updateApplicantGroups(Recruit recruit, List<String> groups) {
//        // 기존 그룹 삭제 및 새로운 그룹 등록 처리
//        recruit.setGroupList(groups);
//        recruitRepository.save(recruit);
//    }


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
    public PrepDetailsDto getPrepDetails(Long recruitId) {
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
//        List<PrepStageDto> prepStages = prepStageRepository.findByRecruitId(recruitId).stream()
//                .map(prepStage -> {
//                    List<String> adminNames = prepStageClubUserRepository.findByPrepStageId(prepStage.getId()).stream()
//                            .map(prepStageClubUser -> prepStageClubUser.getClubUser().getUser().getName())
//                            .collect(Collectors.toList());
//                    return new PrepStageDto(prepStage.getStageName(), prepStage.getStageOrder(),);
//                }).collect(Collectors.toList());

        // 지원자 그룹 가져오기
        List<String> groups = groupRepository.findByRecruitId(recruitId).stream()
                .map(Group::getName)
                .collect(Collectors.toList());

        // 운영진 리스트 가져오기
//        List<PrepStageDto.AdminInfoDto> adminList = clubUserRepository.findStaffNamesByRecruitId(recruitId).stream()
//                .map(result -> new PrepStageDto.AdminInfoDto(
//                        ((Number) result[0]).longValue(), // 첫 번째 값: Long ID
//                        (String) result[1]               // 두 번째 값: String Name
//                ))
//                .collect(Collectors.toList());

        List<PrepStageDto> prepStages = prepStageRepository.findByRecruitId(recruitId).stream()
                .map(prepStage -> {
                    // 해당 PrepStage와 관련된 AdminInfoDto 목록 필터링
                    List<PrepStageDto.AdminInfoDto> relatedAdmins = prepStageClubUserRepository.findByPrepStageId(prepStage.getId()).stream()
                            .map(prepStageClubUser -> new PrepStageDto.AdminInfoDto(
                                    prepStageClubUser.getClubUser().getId(),
                                    prepStageClubUser.getClubUser().getUser().getName()
                            ))
                            .collect(Collectors.toList());

                    // PrepStageDto 생성 시 adminList 포함
                    return new PrepStageDto(prepStage.getStageName(), prepStage.getStageOrder(), relatedAdmins);
                })
                .collect(Collectors.toList());


        return new PrepDetailsDto(scheduleDto, prepStages, groups);
    }

}
