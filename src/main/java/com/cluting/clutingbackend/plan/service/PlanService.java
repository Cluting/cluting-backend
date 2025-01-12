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
import java.util.Map;
import java.util.stream.Collectors;

import static com.cluting.clutingbackend.global.exception.ErrorCode.*;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class PlanService {

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
                        .collect(toList()) : null)
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
    public void saveRecruitmentStage3(Long recruitId, Plan3RequestDto requestDto) {
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
    public void saveInterviewSetup(Long clubUserId, Long recruitId, InterviewSetupDto requestDto) {
        Recruit recruit = findRecruitOrThrow(recruitId);
        recruit.setIntervieweeCount(requestDto.getInterviewee());
        recruit.setInterviewerCount(requestDto.getInterviewer());
        recruit.setInterviewDuration(requestDto.getInterviewDuration());

        ClubUser clubUser = clubUserRepository.findById(clubUserId)
                        .orElseThrow(()-> new CustomException(CLUB_USER_NOT_FOUND, clubUserId + "에 대한 clubUser 정보가 없습니다"));

        clubUser.setInterviewGroup(requestDto.getGroupName());


        recruitRepository.save(recruit);
    }

    public Plan4ResponseDto getInterviewSetup(Long recruitId) {
        Recruit recruit = findRecruitOrThrow(recruitId);

//        // ClubUser에서 interviewGroup을 기준으로 그룹과 운영진 ID 매핑
//        Map<String, List<Long>> groupAndClubUser = recruit.getClub().getClubUsers().stream()
//                .filter(clubUser -> !clubUser.getInterviewGroup().isEmpty()) // interviewGroup이 설정된 운영진만 필터링
//                .collect(Collectors.groupingBy(
//                        ClubUser::getInterviewGroup, // interviewGroup (그룹 이름)을 키로 설정
//                        Collectors.mapping(ClubUser::getId, Collectors.toList()) // 운영진 ID 리스트 추출
//                ));

        // ClubUser에서 interviewGroup을 기준으로 그룹과 운영진 ID 매핑
        Map<String, List<Long>> groupAndClubUser = recruit.getClub().getClubUsers().stream()
                .filter(clubUser -> clubUser.getInterviewGroup() != null && !clubUser.getInterviewGroup().isEmpty()) // null 및 빈 값 필터링
                .collect(Collectors.groupingBy(
                        ClubUser::getInterviewGroup, // interviewGroup (그룹 이름)을 키로 설정
                        Collectors.mapping(ClubUser::getId, Collectors.toList()) // 운영진 ID 리스트 추출
                ));

        return Plan4ResponseDto.builder()
                .interviewee(recruit.getIntervieweeCount())
                .interviewer(recruit.getInterviewerCount())
                .interviewDuration(recruit.getInterviewDuration())
                .groupAndClubUser(groupAndClubUser)
                .build();
    }




    @Transactional
    public void saveTimeSlots(Long recruitId, List<LocalDateTime> timeSlots, CustomUserDetails currentUser) {

        ClubUser clubUser = clubUserRepository.findByUserId(currentUser.getUser().getId())
                .orElseThrow(() -> new CustomException(CLUB_USER_NOT_FOUND, "ClubUser not found for logged-in user"));

        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(()-> new CustomException(RECRUIT_NOT_FOUND, "Recruit not found with id: " + recruitId));

        timeSlots.forEach(time -> {
            InterviewTimeSlot timeSlot = InterviewTimeSlot.builder()
                    .time(time).clubUser(clubUser).isAssigned(false).recruit(recruit).build();
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
                .collect(toList());

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
                                                .stream().map(Option::getContent).collect(toList()))
                                        .multiSelect(documentQuestion.isMultiSelect())
                                        .build())
                                .collect(toList()))
                        .build())
                .collect(toList());

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
    //PATCH//
    /**
     * Stage 1: 합격 인원 설정하기 (부분 업데이트)
     */
    @Transactional
    public Plan1ResponseDto updatePartialRecruit(Long recruitId, Plan1RequestDto requestDto) {
        Recruit recruit = findRecruitOrThrow(recruitId);

        // Recruit의 필드 업데이트
        if (requestDto.getTotalDocumentPassCount() != null) {
            recruit.setNumDoc(requestDto.getTotalDocumentPassCount());
        }

        if (requestDto.getTotalFinalPassCount() != null) {
            recruit.setNumFinal(requestDto.getTotalFinalPassCount());
        }

        // Group 업데이트
        if (requestDto.getGroupInfos() != null && !requestDto.getGroupInfos().isEmpty()) {
            requestDto.getGroupInfos().forEach(partDto -> {
                Group group = groupRepository.findByRecruitIdAndName(recruitId, partDto.getGroupName())
                        .orElse(Group.builder().recruit(recruit).name(partDto.getGroupName()).build());

                // 각 필드를 부분적으로 업데이트
                if (partDto.getDocumentPassCount() != null) {
                    group.setNumDoc(partDto.getDocumentPassCount());
                }

                if (partDto.getFinalPassCount() != null) {
                    group.setNumFinal(partDto.getFinalPassCount());
                }

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
                        .collect(toList()) : null)
                .build();
    }


    /**
     * Stage 2: 인재상 구축하기 (부분 업데이트)
     */
    @Transactional
    public void updatePartialIdeals(Long recruitId, Plan2RequestDto requestDto) {
        requestDto.getPartIdeals().forEach(partIdeal -> {
            // 그룹 찾기 또는 예외 처리
            Group group = findGroupOrThrow(recruitId, partIdeal.getPartName());

            // 기존 Ideal 목록 가져오기
            List<Ideal> existingIdeals = idealRepository.findByGroupId(group.getId());

            // 요청 데이터의 content와 비교하여 추가 또는 수정
            partIdeal.getContent().forEach(content -> {
                Ideal existingIdeal = existingIdeals.stream()
                        .filter(ideal -> ideal.getContent().equals(content))
                        .findFirst()
                        .orElse(null);

                if (existingIdeal == null) {
                    // 기존에 없는 인재상 추가
                    Ideal newIdeal = Ideal.builder().content(content).group(group).build();
                    idealRepository.save(newIdeal);
                }
            });

            // 기존 목록에서 삭제할 항목 찾기
            List<Ideal> toDelete = existingIdeals.stream()
                    .filter(ideal -> !partIdeal.getContent().contains(ideal.getContent()))
                    .toList();

            // 삭제
            toDelete.forEach(idealRepository::delete);
        });
    }


    /**
     * Stage 3: 공고 작성하기 (부분 업데이트)
     */
    @Transactional
    public void updatePartialRecruitmentStage3(Long recruitId, Plan3RequestDto requestDto) {
        Recruit recruit = findRecruitOrThrow(recruitId);

        // 필요한 필드만 업데이트
        if (requestDto.getTitle() != null) {
            recruit.setTitle(requestDto.getTitle());
        }
        if (requestDto.getRecruitmentNumber() != null) {
            recruit.setNumFinal(requestDto.getRecruitmentNumber());
        }
        if (requestDto.getActivityStart() != null) {
            recruit.setActivityStart(requestDto.getActivityStart());
        }
        if (requestDto.getActivityEnd() != null) {
            recruit.setActivityEnd(requestDto.getActivityEnd());
        }
        if (requestDto.getActivityDay() != null) {
            recruit.setActivityDay(requestDto.getActivityDay());
        }
        if (requestDto.getActivityTime() != null) {
            recruit.setActivityTime(requestDto.getActivityTime());
        }
        if (requestDto.getClubFee() != null) {
            recruit.setClubFee(requestDto.getClubFee());
        }
        if (requestDto.getContent() != null) {
            recruit.setDescription(requestDto.getContent());
        }
        if (requestDto.getImageUrl() != null) {
            recruit.setImage(requestDto.getImageUrl());
        }

        recruitRepository.save(recruit);
    }


    /**
     * Stage 4: 면접 세팅 (부분 업데이트)
     */
//    public void updatePartialInterviewSetup(Long recruitId, InterviewSetupDto dto) {
//        Recruitment recruitment = recruitmentRepository.findById(recruitId)
//                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "모집 정보를 찾을 수 없습니다."));
//
//        if (dto.getInterviewDates() != null && !dto.getInterviewDates().isEmpty()) {
//            recruitment.setInterviewDates(dto.getInterviewDates());
//        }
//
//        recruitmentRepository.save(recruitment);
//    }

    /**
     * Stage 5: 지원서 폼 제작하기 (부분 업데이트)
     */
    @Transactional
    public Plan5ResponseDto updatePartialApplicationForm(Long recruitId, Plan5RequestDto requestDto) {
        Recruit recruit = findRecruitOrThrow(recruitId);

        // Recruit 필드 부분 업데이트
        if (requestDto.getTitle() != null) {
            recruit.setApplicationTitle(requestDto.getTitle());
        }
        if (requestDto.getIsPortfolioRequired() != null) {
            recruit.setIsRequiredPortfolio(requestDto.getIsPortfolioRequired());
        }
        if (requestDto.getMultiApply() != null) {
            recruit.setMultiApply(requestDto.getMultiApply());
        }

        recruitRepository.save(recruit);

        // PartQuestions 업데이트
        if (requestDto.getPartQuestions() != null && !requestDto.getPartQuestions().isEmpty()) {
            requestDto.getPartQuestions().forEach(partQuestion -> {
                Group group = findGroupOrThrow(recruitId, partQuestion.getPartName());

                partQuestion.getQuestions().forEach(question -> {
                    DocumentQuestion existingQuestion = documentQuestionRepository.findByGroupIdAndContent(group.getId(), question.getContent())
                            .orElse(DocumentQuestion.builder()
                                    .group(group)
                                    .content(question.getContent())
                                    .build());

                    // DocumentQuestion 필드 부분 업데이트
                    if (question.getMultiSelect() != null) {
                        existingQuestion.setMultiSelect(question.getMultiSelect());
                    }
                    if (question.getQuestionType() != null) {
                        existingQuestion.setQuestionType(question.getQuestionType());
                    }
                    if (question.getWordLimit() != null) {
                        existingQuestion.setWordLimit(question.getWordLimit());
                    }

                    documentQuestionRepository.save(existingQuestion);

                    // Option 업데이트
                    if (question.getObjects() != null) {
                        List<Option> existingOptions = optionRepository.findByDocumentQuestionId(existingQuestion.getId());

                        // 삭제되지 않은 옵션만 남김
                        existingOptions.stream()
                                .filter(option -> !question.getObjects().contains(option.getContent()))
                                .forEach(optionRepository::delete);

                        // 새 옵션 추가
                        question.getObjects().stream()
                                .filter(option -> existingOptions.stream().noneMatch(existing -> existing.getContent().equals(option)))
                                .forEach(option -> {
                                    Option optionEntity = Option.builder().documentQuestion(existingQuestion).content(option).build();
                                    optionRepository.save(optionEntity);
                                });
                    }
                });
            });
        }

        return Plan5ResponseDto.builder()
                .title(recruit.getApplicationTitle())
                .partQuestions(requestDto.getPartQuestions())
                .multiApply(recruit.isMultiApply())
                .isPortfolioRequired(recruit.getIsRequiredPortfolio())
                .build();
    }


    @Transactional
    public void assignTimeSlots(InterviewerAssignedDto interviewerAssignedDto, CustomUserDetails currentUser) {
        // 현재 사용자로 ClubUser 조회
        ClubUser currentClubUser = clubUserRepository.findByUserId(currentUser.getUser().getId())
                .orElseThrow(() -> new CustomException(GROUP_NOT_FOUND, "ClubUser not found for logged-in user"));

        // 시간대별로 면접관 할당
        interviewerAssignedDto.getTimeSlotAssignments().forEach(assignment -> {
            // 시간대 조회
            InterviewTimeSlot timeSlot = interviewTimeSlotRepository.findByTimeAndClubUser(assignment.getTimeSlot(), currentClubUser)
                    .orElseThrow(() -> new CustomException(TIMESLOT_NOT_FOUND, "Time slot not found for this club user"));

            // 면접관 리스트 조회
            List<ClubUser> interviewers = clubUserRepository.findAllById(assignment.getInterviewerIds());

            if (interviewers.isEmpty()) {
                throw new CustomException(CLUB_USER_NOT_FOUND, "No valid interviewers found for the provided IDs.");
            }

            // 면접관 배정
            timeSlot.setInterviewers(interviewers);

            // 상태 변경: isAssigned를 true로 설정
            timeSlot.setAssigned(true);

            // 저장
            interviewTimeSlotRepository.save(timeSlot);
        });
    }


    @Transactional
    public InterviewTimeSlotResponseDto getTimeSlots(Long recruitId) {
        // Recruit 존재 여부 확인
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new CustomException(RECRUIT_NOT_FOUND, "Recruit not found for the given ID"));

        // 시간대와 관련된 정보 가져오기
        List<InterviewTimeSlot> timeSlots = interviewTimeSlotRepository.findByRecruit(recruit);

        // 시간대 데이터를 DTO로 변환
        List<InterviewTimeSlotResponseDto.TimeSlotInfo> timeSlotInfos = timeSlots.stream()
                .map(timeSlot -> {
                    // 면접관 정보 추출
                    List<InterviewTimeSlotResponseDto.InterviewerInfo> interviewers = timeSlot.getInterviewers().stream()
                            .map(interviewer -> InterviewTimeSlotResponseDto.InterviewerInfo.builder()
                                    .id(interviewer.getId())
                                    .name(interviewer.getUser().getName()) // ClubUser와 User 연관 매핑 가정
                                    .groupName(interviewer.getInterviewGroup())
                                    .build())
                            .toList();

                    // 시간대 정보 생성
                    return InterviewTimeSlotResponseDto.TimeSlotInfo.builder()
                            .timeSlot(timeSlot.getTime())
                            .isAssigned(timeSlot.isAssigned())
                            .interviewers(interviewers)
                            .build();
                })
                .toList();

        return InterviewTimeSlotResponseDto.builder()
                .recruitId(recruitId)
                .timeSlots(timeSlotInfos)
                .build();
    }







}

