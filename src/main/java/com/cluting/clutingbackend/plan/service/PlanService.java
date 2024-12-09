package com.cluting.clutingbackend.plan.service;

import com.cluting.clutingbackend.club.domain.Club;
import com.cluting.clutingbackend.club.repository.ClubRepository;
import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.clubuser.repository.ClubUserRepository;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.interview.domain.InterviewTimeSlot;
import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.plan.domain.TalentProfile;
import com.cluting.clutingbackend.plan.dto.request.*;
import com.cluting.clutingbackend.plan.dto.response.Plan1ResponseDto;
import com.cluting.clutingbackend.plan.dto.response.Plan3ResponseDto;
import com.cluting.clutingbackend.plan.dto.response.Plan5ResponseDto;
import com.cluting.clutingbackend.plan.repository.GroupRepository;
import com.cluting.clutingbackend.plan.repository.InterviewTimeSlotRepository;
import com.cluting.clutingbackend.plan.repository.TalentProfileRepository;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.recruit.domain.RecruitSchedule;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import com.cluting.clutingbackend.recruit.repository.RecruitScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final GroupRepository groupRepository;
    private final TalentProfileRepository talentProfileRepository;
    private final RecruitRepository recruitRepository;
    private final RecruitScheduleRepository recruitScheduleRepository;
    private final InterviewTimeSlotRepository interviewTimeSlotRepository;
    private final ClubUserRepository clubUserRepository;

    @Transactional
    public Plan1ResponseDto createRecruitment(Long recruitId, Plan1RequestDto requestDto) {
        // Recruit 엔티티 조회
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new IllegalArgumentException("Recruit not found with id: " + recruitId));

        // Recruit 엔티티 업데이트
        recruit.setNumDoc(requestDto.getTotalDocumentPassCount());
        recruit.setNumFinal(requestDto.getTotalFinalPassCount());

        // 파트별 정보가 있다면 Group 테이블 업데이트
        if (requestDto.getGroupInfos() != null && !requestDto.getGroupInfos().isEmpty()) {
            requestDto.getGroupInfos().forEach(partDto -> {
                // 해당 파트가 존재하는지 확인
                Group group = groupRepository.findByRecruitIdAndName(recruitId, partDto.getGroupName())
                        .orElse(Group.builder()
                                .recruit(recruit)
                                .name(partDto.getGroupName())
                                .build());

                // Group 정보 업데이트
                group.setNumDoc(partDto.getDocumentPassCount());
                group.setNumFinal(partDto.getFinalPassCount());

                groupRepository.save(group); // 업데이트 또는 생성
            });
        }

        // ResponseDto 변환 및 반환
        return Plan1ResponseDto.builder()
                .recruitId(recruit.getId())
                .title(recruit.getTitle())
                .description(recruit.getDescription())
                .totalDocumentPassCount(recruit.getNumDoc())
                .totalFinalPassCount(recruit.getNumFinal())
                .parts(recruit.getGroupList() != null ? recruit.getGroupList().stream().map(group ->
                        Plan1ResponseDto.PartInfo.builder()
                                .partName(group.getName())
                                .documentPassCount(group.getNumDoc())
                                .finalPassCount(group.getNumFinal())
                                .build()
                ).toList() : null)
                .build();
    }

    @Transactional
    public void saveTalentProfiles(Long recruitId, Plan2RequestDto requestDto) {

        //  인재상 저장
        if (requestDto.getPartProfiles() != null) {
            requestDto.getPartProfiles().forEach(partProfile -> {
                Group group = groupRepository.findByRecruitIdAndName(recruitId, partProfile.getPartName())
                        .orElseThrow(() -> new IllegalArgumentException("Group not found for part: " + partProfile.getPartName()));

                partProfile.getProfiles().forEach(profile -> {
                    TalentProfile talentProfile = TalentProfile.builder()
                            .profile(profile)
                            .group(group) // 파트별 인재상은 group 설정
                            .build();
                    talentProfileRepository.save(talentProfile);
                });
            });
        }
    }

    @Transactional
    public void updateRecruitmentStage3(Long recruitId, Plan3RequestDto requestDto) {
        // Recruit 엔티티 조회
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new IllegalArgumentException("Recruit not found with id: " + recruitId));

        // Recruit 엔티티 업데이트
        recruit.setTitle(requestDto.getTitle()); // 공고 제목
        recruit.setNumFinal(requestDto.getRecruitmentNumber()); // 모집 인원
        recruit.setActivityStart(requestDto.getActivityStart()); // 활동 시작일
        recruit.setActivityEnd(requestDto.getActivityEnd()); // 활동 종료알
        recruit.setActivityDay(requestDto.getActivityDay()); // 활동요일
        recruit.setActivityTime(requestDto.getActivityTime()); // 활동시간대
        recruit.setClubFee(requestDto.getClubFee()); // 동아리 회비
        recruit.setDescription(requestDto.getContent()); // 본문(내용)
        recruit.setImage(requestDto.getImageUrl());

        recruitRepository.save(recruit);
    }

    public Plan3ResponseDto showSchedule(Long recruitId) {

        RecruitSchedule recruitSchedule = recruitScheduleRepository.findByRecruitId(recruitId)
                .orElseThrow(() -> new IllegalArgumentException("RecruitSchedule not found with id: " + recruitId));

        return Plan3ResponseDto.builder()
                .DocStart(recruitSchedule.getStage5Start())
                .FinalStart(recruitSchedule.getStage8Start())
                .RecruitStart(recruitSchedule.getStage3Start())
                .RecruitEnd(recruitSchedule.getStage3End())
                .build();

    }

    @Transactional
    public void saveInterviewSetup(Long recruitId, InterviewSetupDto requestDto) {
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new IllegalArgumentException("Recruit not found with id: " + recruitId));

        recruit.setIntervieweeCount(requestDto.getInterviewee());
        recruit.setInterviewerCount(requestDto.getInterviewer());
        recruit.setInterviewDuration(requestDto.getInterviewDuration());

        recruitRepository.save(recruit);
    }

    @Transactional
    public void saveTimeSlots(List<LocalDateTime> timeSlots, @AuthenticationPrincipal CustomUserDetails currentUser) {
        // 현재 로그인한 유저의 ClubUser 조회
        ClubUser clubUser = clubUserRepository.findById(currentUser.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("ClubUser not found for logged-in user"));

        // 각 시간대 저장
        timeSlots.forEach(time -> {
            InterviewTimeSlot timeSlot = InterviewTimeSlot.builder()
                    .time(time)
                    .clubUser(clubUser)
                    .isAssigned(false) // 초기값은 할당되지 않음
                    .build();
            interviewTimeSlotRepository.save(timeSlot);
        });
    }

    @Transactional
    public Plan5ResponseDto createApplicationForm(Long recruitId, Plan5RequestDto requestDto) {
        // Recruit 엔티티 조회
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new IllegalArgumentException("Recruit not found with id: " + recruitId));

        // Recruit 업데이트
        recruit.setApplicationTitle(requestDto.getTitle());
        recruit.setIsRequiredPortfolio(requestDto.getIsPortfolioRequired());

        recruitRepository.save(recruit);

        // DTO 변환 및 반환
        return Plan5ResponseDto.builder()
                .title(requestDto.getTitle())
                .partQuestions(requestDto.getPartQuestions())
                .isPortfolioRequired(requestDto.getIsPortfolioRequired())
                .build();
    }

}
