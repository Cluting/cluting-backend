package com.cluting.clutingbackend.plan.service;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.clubuser.repository.ClubUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.cluting.clutingbackend.global.exception.CustomException;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.interview.domain.InterviewTimeSlot;
import com.cluting.clutingbackend.plan.domain.*;
import com.cluting.clutingbackend.plan.dto.request.*;
import com.cluting.clutingbackend.plan.dto.response.*;
import com.cluting.clutingbackend.plan.repository.*;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.recruit.domain.RecruitSchedule;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import com.cluting.clutingbackend.recruit.repository.RecruitScheduleRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.cluting.clutingbackend.global.exception.ErrorCode.GROUP_NOT_FOUND;
import static com.cluting.clutingbackend.global.exception.ErrorCode.RECRUIT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PlanService2 {

    private final GroupRepository groupRepository;
    private final IdealRepository idealRepository;
    private final RecruitRepository recruitRepository;
    private final RecruitScheduleRepository recruitScheduleRepository;
    private final InterviewTimeSlotRepository interviewTimeSlotRepository;
    private final ClubUserRepository clubUserRepository;
    private final DocumentQuestionRepository documentQuestionRepository;
    private final OptionRepository optionRepository;

    // 공통적으로 Recruit 조회하는 유틸리티 메서드
    private Recruit findRecruitOrThrow(Long recruitId) {
        return recruitRepository.findById(recruitId)
                .orElseThrow(() -> new CustomException(RECRUIT_NOT_FOUND, "Recruit not found with id: " + recruitId));
    }

    // 공통적으로 Group 조회하는 유틸리티 메서드
    private Group findGroupOrThrow(Long recruitId, String groupName) {
        return groupRepository.findByRecruitIdAndName(recruitId, groupName)
                .orElseThrow(() -> new CustomException(GROUP_NOT_FOUND, "Group not found for recruitId: " + recruitId + ", groupName: " + groupName));
    }

    @Transactional
    public Plan1ResponseDto createRecruitment(Long recruitId, Plan1RequestDto requestDto) {
        Recruit recruit = findRecruitOrThrow(recruitId);

        // Recruit 업데이트
        recruit.setNumDoc(requestDto.getTotalDocumentPassCount());
        recruit.setNumFinal(requestDto.getTotalFinalPassCount());

        // Group 업데이트
        if (requestDto.getGroupInfos() != null && !requestDto.getGroupInfos().isEmpty()) {
            requestDto.getGroupInfos().forEach(partDto -> {
                Group group = groupRepository.findByRecruitIdAndName(recruitId, partDto.getGroupName())
                        .orElse(Group.builder().recruit(recruit).name(partDto.getGroupName()).build());
                group.setNumDoc(partDto.getDocumentPassCount());
                group.setNumFinal(partDto.getFinalPassCount());
                groupRepository.save(group);
            });
        }

        // DTO 반환
        return Plan1ResponseDto.builder()
                .recruitId(recruit.getId())
                .title(recruit.getTitle())
                .description(recruit.getDescription())
                .totalDocumentPassCount(recruit.getNumDoc())
                .totalFinalPassCount(recruit.getNumFinal())
                .parts(recruit.getGroupList() != null ? recruit.getGroupList().stream()
                        .map(group -> Plan1ResponseDto.PartInfo.builder()
                                .partName(group.getName())
                                .documentPassCount(group.getNumDoc())
                                .finalPassCount(group.getNumFinal())
                                .build())
                        .collect(Collectors.toList()) : null)
                .build();
    }

    @Transactional
    public void saveIdeals(Long recruitId, Plan2RequestDto requestDto) {
        requestDto.getPartIdeals().forEach(partIdeal -> {
            Group group = findGroupOrThrow(recruitId, partIdeal.getPartName());
            partIdeal.getContent().forEach(content -> {
                Ideal ideal = Ideal.builder().content(content).group(group).build();
                idealRepository.save(ideal);
            });
        });
    }

    @Transactional
    public void updateRecruitmentStage3(Long recruitId, Plan3RequestDto requestDto) {
        Recruit recruit = findRecruitOrThrow(recruitId);
        recruit.updateRecruitDetails(requestDto);
        recruitRepository.save(recruit);
    }

    public Plan3ResponseDto showSchedule(Long recruitId) {
        RecruitSchedule schedule = recruitScheduleRepository.findByRecruitId(recruitId)
                .orElseThrow(() -> new CustomException(RECRUIT_NOT_FOUND, "Schedule not found for recruitId: " + recruitId));

        return Plan3ResponseDto.builder()
                .DocStart(schedule.getStage5Start())
                .FinalStart(schedule.getStage8Start())
                .RecruitStart(schedule.getStage3Start())
                .RecruitEnd(schedule.getStage3End())
                .build();
    }

    @Transactional
    public void saveInterviewSetup(Long recruitId, InterviewSetupDto requestDto) {
        Recruit recruit = findRecruitOrThrow(recruitId);
        recruit.setIntervieweeCount(requestDto.getInterviewee());
        recruit.setInterviewerCount(requestDto.getInterviewer());
        recruit.setInterviewDuration(requestDto.getInterviewDuration());
        recruitRepository.save(recruit);
    }

    public InterviewSetupDto getInterviewSetup(Long recruitId) {
        Recruit recruit = findRecruitOrThrow(recruitId);
        return InterviewSetupDto.builder()
                .interviewee(recruit.getIntervieweeCount())
                .interviewer(recruit.getInterviewerCount())
                .interviewDuration(recruit.getInterviewDuration())
                .build();
    }

    @Transactional
    public void saveTimeSlots(List<LocalDateTime> timeSlots, CustomUserDetails currentUser) {
        ClubUser clubUser = clubUserRepository.findByUserId(currentUser.getUser().getId())
                .orElseThrow(() -> new CustomException(GROUP_NOT_FOUND, "ClubUser not found for logged-in user"));

        timeSlots.forEach(time -> {
            InterviewTimeSlot timeSlot = InterviewTimeSlot.builder()
                    .time(time).clubUser(clubUser).isAssigned(false).build();
            interviewTimeSlotRepository.save(timeSlot);
        });
    }

    @Transactional
    public Plan5ResponseDto createApplicationForm(Long recruitId, Plan5RequestDto requestDto) {
        Recruit recruit = findRecruitOrThrow(recruitId);

        recruit.setApplicationTitle(requestDto.getTitle());
        recruit.setIsRequiredPortfolio(requestDto.getIsPortfolioRequired());
        recruit.setMultiApply(requestDto.getMultiApply());
        recruitRepository.save(recruit);

        requestDto.getPartQuestions().forEach(partQuestion -> {
            Group group = findGroupOrThrow(recruitId, partQuestion.getPartName());
            partQuestion.getQuestions().forEach(question -> {
                DocumentQuestion documentQuestion = DocumentQuestion.builder()
                        .group(group).content(question.getContent())
                        .multiSelect(question.getMultiSelect())
                        .questionType(question.getQuestionType())
                        .wordLimit(question.getWordLimit())
                        .build();
                documentQuestionRepository.save(documentQuestion);

                if (question.getObjects() != null) {
                    question.getObjects().forEach(option -> {
                        Option optionEntity = Option.builder().documentQuestion(documentQuestion).content(option).build();
                        optionRepository.save(optionEntity);
                    });
                }
            });
        });

        return Plan5ResponseDto.builder()
                .title(recruit.getApplicationTitle())
                .partQuestions(requestDto.getPartQuestions())
                .multiApply(recruit.isMultiApply())
                .isPortfolioRequired(recruit.getIsRequiredPortfolio())
                .build();
    }

    public RecruitDetailResponseDto getRecruitDetails(Long recruitId) {
        Recruit recruit = findRecruitOrThrow(recruitId);
        List<RecruitDetailResponseDto.GroupResponse> groupResponses = recruit.getGroupList().stream()
                .map(group -> new RecruitDetailResponseDto.GroupResponse(
                        group.getId(),
                        group.getIdealList().stream().collect(Collectors.toMap(Ideal::getId, Ideal::getContent)),
                        group.getNumDoc(), group.getNumFinal()))
                .collect(Collectors.toList());

        return new RecruitDetailResponseDto(
                recruit.getId(),
                recruit.getTitle(),
                recruit.getNumDoc(),
                recruit.getNumFinal(),
                groupResponses
        );
    }

    public Plan5ResponseDto getFormDetail(Long recruitId) {
        Recruit recruit = findRecruitOrThrow(recruitId);
        List<Plan5RequestDto.PartQuestionDto> partQuestions = recruit.getGroupList().stream()
                .map(group -> Plan5RequestDto.PartQuestionDto.builder()
                        .partName(group.getName())
                        .caution(group.getWarning())
                        .questions(documentQuestionRepository.findByGroupId(group.getId()).stream()
                                .map(documentQuestion -> Plan5RequestDto.QuestionDto.builder()
                                        .content(documentQuestion.getContent())
                                        .questionType(documentQuestion.getQuestionType())
                                        .hasWordLimit(documentQuestion.getWordLimit() != null)
                                        .wordLimit(documentQuestion.getWordLimit())
                                        .objects(optionRepository.findByDocumentQuestionId(documentQuestion.getId())
                                                .stream().map(Option::getContent).collect(Collectors.toList()))
                                        .multiSelect(documentQuestion.isMultiSelect())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return Plan5ResponseDto.builder()
                .title(recruit.getApplicationTitle())
                .partQuestions(partQuestions)
                .multiApply(recruit.isMultiApply())
                .isPortfolioRequired(recruit.getIsRequiredPortfolio())
                .build();
    }

    public Plan3RequestDto getRecruitmentStage3(Long recruitId) {
        Recruit recruit = findRecruitOrThrow(recruitId);
        RecruitSchedule schedule = recruitScheduleRepository.findByRecruitId(recruitId)
                .orElseThrow(() -> new CustomException(RECRUIT_NOT_FOUND, "Recruitment schedule not found for id: " + recruitId));

        return Plan3RequestDto.builder()
                .title(recruit.getTitle())
                .recruitmentStartDate(schedule.getStage3Start())
                .recruitmentEndDate(schedule.getStage3End())
                .documentResultDate(schedule.getStage5Start())
                .finalResultDate(schedule.getStage8Start())
                .recruitmentNumber(recruit.getNumFinal())
                .activityStart(recruit.getActivityStart())
                .activityEnd(recruit.getActivityEnd())
                .activityDay(recruit.getActivityDay())
                .activityTime(recruit.getActivityTime())
                .clubFee(recruit.getClubFee())
                .content(recruit.getDescription())
                .imageUrl(recruit.getImage())
                .build();
    }
}

