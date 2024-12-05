package com.cluting.clutingbackend.prep.service;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.clubuser.repository.ClubUserRepository;
import com.cluting.clutingbackend.global.enums.CurrentStage;
import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.plan.repository.GroupRepository;
import com.cluting.clutingbackend.prep.domain.PrepStage;
import com.cluting.clutingbackend.prep.domain.PrepStageClubUser;
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

@Service
@RequiredArgsConstructor
public class PrepService {
    private final RecruitScheduleRepository recruitScheduleRepository;
    private final RecruitRepository recruitRepository;
    private final PrepStageRepository prepStageRepository;
    private final PrepStageClubUserRepository prepStageClubUserRepository;
    private final ClubUserRepository clubUserRepository;
    private final GroupRepository groupRepository;

    // [계획하기] 설정 완료하기
    @Transactional
    public void savePreparation(Long recruitId, PrepRequestDto prepRequestDto) {
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모집 공고를 찾을 수 없습니다. id: " + recruitId));

        // 1. 리크루팅 일정 저장
        RecruitSchedule recruitSchedule = recruitScheduleRepository.findByRecruitId(recruitId)
                .orElse(new RecruitSchedule());

        recruitSchedule.setRecruit(recruit);

        recruitSchedule.setStage1Start(prepRequestDto.getRecruitSchedules().get(0).getStage1Start());
        recruitSchedule.setStage1End(prepRequestDto.getRecruitSchedules().get(0).getStage1End());
        recruitSchedule.setStage2Start(prepRequestDto.getRecruitSchedules().get(0).getStage2Start());
        recruitSchedule.setStage2End(prepRequestDto.getRecruitSchedules().get(0).getStage2End());
        recruitSchedule.setStage3Start(prepRequestDto.getRecruitSchedules().get(0).getStage3Start());
        recruitSchedule.setStage3End(prepRequestDto.getRecruitSchedules().get(0).getStage3End());
        recruitSchedule.setStage4Start(prepRequestDto.getRecruitSchedules().get(0).getStage4Start());
        recruitSchedule.setStage4End(prepRequestDto.getRecruitSchedules().get(0).getStage4End());
        recruitSchedule.setStage5Start(prepRequestDto.getRecruitSchedules().get(0).getStage5Start());
        recruitSchedule.setStage5End(prepRequestDto.getRecruitSchedules().get(0).getStage5End());
        recruitSchedule.setStage6Start(prepRequestDto.getRecruitSchedules().get(0).getStage6Start());
        recruitSchedule.setStage6End(prepRequestDto.getRecruitSchedules().get(0).getStage6End());

        recruitScheduleRepository.save(recruitSchedule);

        // 2. 모집 단계 및 운영진 저장
        if (!prepStageRepository.findByRecruitId(recruitId).isEmpty()) {
            prepStageRepository.deleteByRecruitId(recruitId);
        }

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
        if (!groupRepository.findByRecruitId(recruitId).isEmpty()) {
            groupRepository.deleteByRecruitId(recruitId);
        }

        List<String> applicantGroups = prepRequestDto.getApplicantGroups();
        if (applicantGroups.isEmpty()) {
            applicantGroups = List.of("공통");
        }

        for (String groupName : applicantGroups) {
            Group group = new Group();
            group.setRecruit(recruit);
            group.setName(groupName);
            groupRepository.save(group);
        }

        // 4. 현재 진행 중인 리크루팅 단계 PREP으로 변경
        recruit.setCurrentStage(CurrentStage.PREP);
        recruitRepository.save(recruit);
    }
}
