package com.cluting.clutingbackend.evaluation.service;

import com.cluting.clutingbackend.application.domain.ApplicantInterviewTimeSlot;
import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.repository.ApplicantInterviewTimeSlotRepository;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.clubuser.dto.response.ClubUserResponseDto;
import com.cluting.clutingbackend.clubuser.repository.ClubUserRepository;
import com.cluting.clutingbackend.evaluation.dto.request.InterviewIndividualQuestionRequestDto;
import com.cluting.clutingbackend.evaluation.dto.request.InterviewQuestionSaveRequestDto;
import com.cluting.clutingbackend.evaluation.dto.request.MessageSendRequestDto;
import com.cluting.clutingbackend.evaluation.dto.request.ScheduleFormDataRequestDto;
import com.cluting.clutingbackend.evaluation.dto.response.*;
import com.cluting.clutingbackend.evaluation.dto.GroupResponse;
import com.cluting.clutingbackend.evaluation.dto.document.ApplicantInfo;
import com.cluting.clutingbackend.evaluation.dto.interview.*;
import com.cluting.clutingbackend.global.enums.*;
import com.cluting.clutingbackend.global.message.MessageUtil;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.interview.domain.*;
import com.cluting.clutingbackend.interview.repository.*;
import com.cluting.clutingbackend.plan.domain.DocumentEvaluator;
import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.plan.domain.Ideal;
import com.cluting.clutingbackend.plan.repository.DocumentEvaluatorRepository;
import com.cluting.clutingbackend.plan.repository.GroupRepository;
import com.cluting.clutingbackend.plan.repository.IdealRepository;
import com.cluting.clutingbackend.plan.repository.InterviewTimeSlotRepository;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.recruit.dto.response.RecruitNumResponseDto;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.user.dto.response.UserResponseDto;
import com.cluting.clutingbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewEvaluationService {
    private final RecruitRepository recruitRepository;
    private final GroupRepository groupRepository;
    private final ApplicationRepository applicationRepository;
    private final DocumentEvaluatorRepository documentEvaluatorRepository;
    private final ClubUserRepository clubUserRepository;
    private final InterviewRepository interviewRepository;
    private final InterviewEvaluatorRepository interviewEvaluatorRepository;
    private final InterviewCriteriaRepository interviewCriteriaRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final UserRepository userRepository;
    private final IdealRepository idealRepository;
    private final InterviewScoreRepository interviewScoreRepository;
    private final InterviewTimeSlotRepository interviewTimeSlotRepository;
    private final ApplicantInterviewTimeSlotRepository applicantInterviewTimeSlotRepository;
    private final MessageUtil messageUtil;

    // 메시지 일괄 전송
    @Transactional(readOnly = true)
    public void send(Long recruitId, MessageSendRequestDto messageSendRequestDto, EvaluateStatus status) {
        List<Interview> interviews = interviewRepository.findAllByApplication_Recruit_Id(recruitId);
        for (Interview interview : interviews) {
            String individual = messageSendRequestDto.getMessage();
            individual = individual.replace("{{이름}}", interview.getApplication().getUser().getName()).replace("{{파트}}", interview.getApplication().getRecruit_group());
            messageUtil.send(interview.getApplication().getUser().getPhone(), individual);
        }
    }

    // 면접 결과 리스트
    @Transactional(readOnly = true)
    public List<InterviewResultListResponseDto> getList(Long recruitId, EvaluateStatus status) {
        List<Application> applications = applicationRepository.findByRecruitId(recruitId)
                .stream()
                .filter(application -> status.equals(application.getState()))
                .toList();

        List<Interview> interviews = interviewRepository.findAllByApplication_Recruit_Id(recruitId);
        List<InterviewResultListResponseDto> result = new ArrayList<>();
        for (Interview interview : interviews) {
            result.add(InterviewResultListResponseDto.toDto(interview));
        }

        return result;
    }

    public InterviewClassifyResponseDto findInterviewAvailable(Long recruitId, String partName) {
        List<InterviewTimeSlot> staffTimeSlot = interviewTimeSlotRepository.findAllByRecruit_Id(recruitId); // 운영진 면접 가능 시간
        List<ApplicantInterviewTimeSlot> applicantTimeSlot = applicantInterviewTimeSlotRepository.findAllByApplication_Recruit_Id(recruitId); // 지원자 면접 가능 시간

        InterviewClassifyResponseDto interviewClassifyResponseDto = new InterviewClassifyResponseDto();
        Map<LocalDateTime, InterviewClassifyResponseDto.InterviewClassify> map = new HashMap<>();

        for (InterviewTimeSlot interviewTimeSlot : staffTimeSlot) {
            LocalDateTime timeSlot = interviewTimeSlot.getTime();
            InterviewClassifyResponseDto.InterviewClassify classify = map.getOrDefault(timeSlot, new InterviewClassifyResponseDto.InterviewClassify());

            List<InterviewClassifyResponseDto.InterviewAssign> staffList = classify.getStaff();
            if (staffList == null) {
                staffList = new ArrayList<>();
            }

            InterviewClassifyResponseDto.InterviewAssign staffAssign = new InterviewClassifyResponseDto.InterviewAssign();
            staffAssign.setId(interviewTimeSlot.getClubUser().getId());
            staffAssign.setName(interviewTimeSlot.getClubUser().getUser().getName());
            staffList.add(staffAssign);

            classify.setStaff(staffList);
            map.put(timeSlot, classify);
        }

        for (ApplicantInterviewTimeSlot applicantInterviewTimeSlot : applicantTimeSlot) {
            LocalDateTime timeSlot = applicantInterviewTimeSlot.getTime();
            InterviewClassifyResponseDto.InterviewClassify classify = map.getOrDefault(timeSlot, new InterviewClassifyResponseDto.InterviewClassify());

            List<InterviewClassifyResponseDto.InterviewAssign> applicantList = classify.getApplicant();
            if (applicantList == null) {
                applicantList = new ArrayList<>();
            }

            if (applicantInterviewTimeSlot.getApplication().getRecruit_group().equals(partName)) { // 파트 이름 검사
                InterviewClassifyResponseDto.InterviewAssign applicantAssign = new InterviewClassifyResponseDto.InterviewAssign();
                applicantAssign.setId(applicantInterviewTimeSlot.getApplication().getUser().getId());
                applicantAssign.setName(applicantInterviewTimeSlot.getApplication().getUser().getName());
                applicantList.add(applicantAssign);

                classify.setApplicant(applicantList);
                map.put(timeSlot, classify);
            }
        }

        interviewClassifyResponseDto.setList(map);
        return interviewClassifyResponseDto;
    }

    @Transactional(readOnly = true)
    public InterviewEvaluationResultResponseDto findInterviewPassAndFail(Long recruitId, SortType sortType) {
        List<Interview> interviews = interviewRepository.findAllByApplication_Recruit_Id(recruitId);

        int passCnt = 0;
        int failCnt = 0;
        Map<String, Integer> cnt = new HashMap<>();
        List<InterviewEvaluationResultResponseDto.InterviewEvaluateResult> passed = new ArrayList<>();
        List<InterviewEvaluationResultResponseDto.InterviewEvaluateResult> failed = new ArrayList<>();

        for (Interview interview : interviews) {
            if (interview.getState() == EvaluateStatus.PASS) {
                passCnt++;
                String groupName = interview.getRecruit_group();
                cnt.put(groupName, cnt.getOrDefault(groupName, 0) + 1);
                InterviewEvaluationResultResponseDto.InterviewEvaluateResult result = InterviewEvaluationResultResponseDto.InterviewEvaluateResult.toDto(
                        interview,
                        Stage.AFTER,
                        "합격"
                );
                passed.add(result);
            } else if (interview.getState() == EvaluateStatus.FAIL) {
                failCnt++;
                InterviewEvaluationResultResponseDto.InterviewEvaluateResult result = InterviewEvaluationResultResponseDto.InterviewEvaluateResult.toDto(
                        interview,
                        Stage.AFTER,
                        "불합격"
                );
                failed.add(result);
            }
        }

        sortInterviewAndAssignRank(passed);
        sortInterviewAndAssignRank(failed);

        if (sortType == SortType.NEWEST) {
            passed.sort(Comparator.comparing(InterviewEvaluationResultResponseDto.InterviewEvaluateResult::getCreatedAt).reversed());
            failed.sort(Comparator.comparing(InterviewEvaluationResultResponseDto.InterviewEvaluateResult::getCreatedAt).reversed());
        } else if (sortType == SortType.OLDEST) {
            passed.sort(Comparator.comparing(InterviewEvaluationResultResponseDto.InterviewEvaluateResult::getCreatedAt));
            failed.sort(Comparator.comparing(InterviewEvaluationResultResponseDto.InterviewEvaluateResult::getCreatedAt));
        } else if (sortType == SortType.INORDER) {
            passed.sort(Comparator.comparing(InterviewEvaluationResultResponseDto.InterviewEvaluateResult::getName));
            failed.sort(Comparator.comparing(InterviewEvaluationResultResponseDto.InterviewEvaluateResult::getName));
        } else {
            throw new IllegalArgumentException("정의되지 않은 정렬 방식 입니다.");
        }

        return InterviewEvaluationResultResponseDto.builder()
                .passedCnt(passCnt)
                .byGroup(cnt)
                .passed(passed)
                .failedCnt(failCnt)
                .failed(failed)
                .build();
    }

    private void sortInterviewAndAssignRank(List<InterviewEvaluationResultResponseDto.InterviewEvaluateResult> list) {
        list.sort((o1, o2) -> Integer.compare(o2.getScore(), o1.getScore())); // 내림차순 정렬
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setRank(i + 1); // 1부터 시작하는 순위 설정
        }
    }

    @Transactional(readOnly = true)
    public InterviewAvailScheduleResponseDto findSchedules(Long recruitId) {
        List<InterviewTimeSlot> interviewTimeSlots = interviewTimeSlotRepository.findAllByRecruit_Id(recruitId); // 운영진 면접 가능 시간
        List<ApplicantInterviewTimeSlot> applicantInterviewTimeSlots = applicantInterviewTimeSlotRepository.findAllByApplication_Recruit_Id(recruitId); // 지원자 면접 가능 시간

        Map<String, InterviewAvailScheduleResponseDto.Schedule> schedules = new HashMap<>(); // 그룹이름 : Schedule
        List<String> groups = groupRepository.findByRecruitId(recruitId).stream().map(Group::getName).distinct().toList(); // 그룹 이름 가져오기

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-M-d"); // 날짜 포맷
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a"); // 시간 포맷

        // 모든 면접자 리스트 생성
        List<InterviewAvailScheduleResponseDto.Participant> allInterviewers = interviewTimeSlots.stream()
                .map(slot -> InterviewAvailScheduleResponseDto.Participant.builder()
                        .id(slot.getClubUser().getId())
                        .name(slot.getClubUser().getUser().getName())
                        .groupName(null) // 그룹명 없음
                        .build())
                .toList();

        for (String groupName : groups) {
            Map<String, InterviewAvailScheduleResponseDto.Composed> dates = new HashMap<>();

            // 지원자 리스트 생성
            List<InterviewAvailScheduleResponseDto.Participant> applicants = applicantInterviewTimeSlots.stream()
                    .filter(slot -> slot.getApplication().getRecruit_group().equals(groupName.trim())) // 해당 그룹 필터링
                    .map(slot -> InterviewAvailScheduleResponseDto.Participant.builder()
                            .id(slot.getApplication().getUser().getId())
                            .name(slot.getApplication().getUser().getName())
                            .groupName(groupName)
                            .build())
                    .toList();

            // 날짜와 시간별 구성
            for (ApplicantInterviewTimeSlot timeSlot : applicantInterviewTimeSlots) {
                if (!timeSlot.getApplication().getRecruit_group().equals(groupName.trim())) continue;

                String date = timeSlot.getTime().toLocalDate().format(dateFormatter); // 날짜를 String으로 변환
                String time = timeSlot.getTime().toLocalTime().format(timeFormatter); // 시간을 String으로 변환

                dates.computeIfAbsent(date, k -> InterviewAvailScheduleResponseDto.Composed.builder()
                        .time(timeSlot.getTime())
                        .interviewers(new ArrayList<>(allInterviewers)) // 모든 면접자 추가
                        .applicants(new ArrayList<>())
                        .build());

                // 지원자 추가
                dates.get(date).getApplicants().addAll(applicants);
            }

            // 그룹과 날짜별 스케줄 저장
            schedules.put(groupName, InterviewAvailScheduleResponseDto.Schedule.builder()
                    .dates(dates)
                    .build());
        }

        return InterviewAvailScheduleResponseDto.builder()
                .schedules(schedules)
                .build();
    }


    @Transactional
    public void saveInterviewSchedule(Long recruitId, ScheduleFormDataRequestDto scheduleFormDataRequestDto) {
        List<ApplicantInterviewTimeSlot> applicantInterviewTimeSlots = applicantInterviewTimeSlotRepository.findAllByApplication_Recruit_Id(recruitId);

        Map<Long, ScheduleFormDataRequestDto.Group> map = scheduleFormDataRequestDto.getGroups();
        for (Long groupId : map.keySet()) {
            Group group = findGroupById(groupId);
            ScheduleFormDataRequestDto.Group group1 = map.get(groupId);
            Map<String, ScheduleFormDataRequestDto.DateSchedules> dates = group1.getDates();
            for (String date : dates.keySet()) { // 날짜 순회
                for (ScheduleFormDataRequestDto.Schedule schedule : dates.get(date).getSchedules()) {
                    String time = schedule.getTime();
                    List<Long> applicants = schedule.getApplicants();
                    List<Long> interviewers = schedule.getInterviewers();
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
                    LocalDate localDate = LocalDate.parse(date);
                    LocalTime localTime = LocalTime.parse(time, timeFormatter);
                    LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime); // localdatetime으로 변경
                    for (Long userId : applicants) { // 지원자 순회
                        User user = findByUserId(userId);
                        ApplicantInterviewTimeSlot byTimeAndUserId = applicantInterviewTimeSlotRepository.findByTimeAndUserId(localDateTime, user.getId())
                                .orElseThrow(() -> new IllegalArgumentException("시간표가 존재하지 않습니다."));
                        byTimeAndUserId.setIsAssigned(true);
                        applicantInterviewTimeSlotRepository.save(byTimeAndUserId); // 시간이랑 지원자 id로 검색하여 확정 여부 true로 설정
                    }
                    for (Long clubUserId : interviewers) {
                        ClubUser clubUser = findByClubUserId(clubUserId);
                        InterviewTimeSlot byTimeAndClubUserId = interviewTimeSlotRepository.findByTimeAndClubUserId(localDateTime, clubUserId)
                                .orElseThrow(() -> new IllegalArgumentException("시간표가 존재하지 않습니다."));
                        byTimeAndClubUserId.setIsAssigned(true);
                        interviewTimeSlotRepository.save(byTimeAndClubUserId);
                    }
                }
            }
        }
    }

    public User findByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
    }

    public ClubUser findByClubUserId(Long clubUserId) {
        return clubUserRepository.findById(clubUserId)
                .orElseThrow(() -> new IllegalArgumentException("운영진이 존재하지 않습니다."));
    }

    public Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("지원 그룹이 존재하지 않습니다."));
    }

    public List<InterviewEvaluationResponse> getInterviewEvaluations(Long recruitId, CustomUserDetails currentUser, InterviewEvaluationRequest request) {
        Long currentClubUserId = currentUser.getUser() != null ? currentUser.getUser().getId() : null;

        // Step 1: 모집 공고 ID로 Application 조회
        List<Application> applications = applicationRepository.findByRecruitId(recruitId);
        if (applications == null || applications.isEmpty()) {
            return Collections.emptyList(); // Application이 없으면 빈 리스트 반환
        }

        // Step 2: Application ID로 Interview 조회
        List<Interview> interviews = interviewRepository.findByApplicationIdIn(
                applications.stream().map(Application::getId).collect(Collectors.toList()));
        if (interviews == null || interviews.isEmpty()) {
            return Collections.emptyList(); // Interview가 없으면 빈 리스트 반환
        }

        // Step 3: Interview ID로 InterviewEvaluator 조회 및 단계별 분리
        List<InterviewEvaluator> evaluators = interviewEvaluatorRepository.findByInterviewIdIn(
                interviews.stream().map(Interview::getId).collect(Collectors.toList()));
        if (evaluators == null || evaluators.isEmpty()) {
            return Collections.emptyList(); // Evaluators가 없으면 빈 리스트 반환
        }

        return evaluators.stream()
                .filter(evaluator -> evaluator.getClubUser() != null
                        && evaluator.getClubUser().getUser() != null
                        && evaluator.getClubUser().getUser().getId().equals(currentClubUserId)
                        && (request.getGroupName() == null ||
                        (evaluator.getGroup() != null && evaluator.getGroup().getName() != null
                                && evaluator.getGroup().getName().equals(request.getGroupName()))))
                .map(evaluator -> mapToResponse(evaluator, request.getSortOrder()))
                .sorted((resp1, resp2) -> sortResponses(resp1, resp2, request.getSortOrder()))
                .collect(Collectors.toList());
    }


    private InterviewEvaluationResponse mapToResponse(InterviewEvaluator evaluator, String sortOrder) {
        Interview interview = evaluator.getInterview();
        Application application = interview.getApplication();
        User applicant = application.getUser();

        int totalEvaluators = interviewEvaluatorRepository.countDistinctByInterviewId(interview.getId());
        int currentEvaluators = interview.getNumClubUser() != null ? interview.getNumClubUser() : 0;



        return InterviewEvaluationResponse.builder()
                .stage(evaluator.getStage().name())
                .applicantName(applicant.getName())
                .applicantPhone(applicant.getPhone())
                .groupName(evaluator.getGroup() != null ? evaluator.getGroup().getName() : "N/A")
                .evaluationStatus(currentEvaluators + "/" + totalEvaluators)
                .build();
    }

    private int sortResponses(InterviewEvaluationResponse resp1, InterviewEvaluationResponse resp2, String sortOrder) {
        if ("newest".equals(sortOrder)) {
            return resp2.getStage().compareTo(resp1.getStage());
        } else if ("oldest".equals(sortOrder)) {
            return resp1.getStage().compareTo(resp2.getStage());
        }
        return 0;
    }


    public List<GroupResponse> getGroupsByRecruitId(Long recruitId) {
        // Group 엔티티에서 recruitId로 그룹 조회
        List<Group> groups = groupRepository.findAllByRecruitIdForInterview(recruitId);

        // GroupResponse로 매핑
        return groups.stream()
                .map(group -> new GroupResponse(group.getId(), group.getName()))
                .toList();
    }

    public List<InterviewEvaluationResultDto> evaluateInterviews(Long recruitId, CustomUserDetails currentUser) {
        // 해당 Recruit에 연결된 Application 가져오기
        List<Application> applications = applicationRepository.findByRecruitId(recruitId);

        // Application을 기준으로 Interview 조회
        List<Interview> interviews = interviewRepository.findByApplicationIn(applications);

        // Interview 정렬 및 평가 상태 업데이트
        List<Interview> sortedInterviews = interviews.stream()
                .sorted(Comparator.comparingInt(Interview::getScore).reversed())
                .toList();

        Recruit recruit = applications.get(0).getRecruit(); // 같은 Recruit에 속하므로 하나 가져옴
        int numDoc = recruit.getNumDoc(); // 서류 합격 인원 수

        List<InterviewEvaluationResultDto> results = new ArrayList<>();
        for (int i = 0; i < sortedInterviews.size(); i++) {
            Interview interview = sortedInterviews.get(i);
            if (i < numDoc) {
                interview.setState(EvaluateStatus.PASS);
            } else {
                interview.setState(EvaluateStatus.FAIL);
            }
            results.add(
                    InterviewEvaluationResultDto.builder()
                            .interviewId(interview.getId())
                            .state(interview.getState())
                            .score(interview.getScore())
                            .build()
            );
        }

        interviewRepository.saveAll(sortedInterviews); // 변경 사항 저장
        return results;
    }

    public Map<String, List<InterviewEvaluationCompleteResponse>> getCompletedEvaluations(Long recruitId) {
        // 1. recruitId로 Application 목록 찾기
        List<Application> applications = applicationRepository.findByRecruitId(recruitId);
        if (applications == null || applications.isEmpty()) {
            return new HashMap<>();  // applications가 null이거나 비어 있으면 빈 맵 반환
        }

        // 2. 합격(PASS)과 불합격(FAIL) 상태로 Interview 분리
        List<Interview> interviews = interviewRepository.findByApplicationIn(applications);
        if (interviews == null) {
            interviews = new ArrayList<>();  // interviews가 null이면 빈 리스트로 초기화
        }

        List<InterviewEvaluationCompleteResponse> passedEvaluations = new ArrayList<>();
        List<InterviewEvaluationCompleteResponse> failedEvaluations = new ArrayList<>();

        for (Interview interview : interviews) {
            if (interview == null) continue;  // interview가 null이면 건너뜀

            // 3. Interview의 상태에 따라 처리
            if (EvaluateStatus.PASS.equals(interview.getState())) {
                passedEvaluations.add(mapToEvaluationResponse(interview));
            } else if (EvaluateStatus.FAIL.equals(interview.getState())) {
                failedEvaluations.add(mapToEvaluationResponse(interview));
            }
        }

        // 4. 결과 맵핑
        Map<String, List<InterviewEvaluationCompleteResponse>> result = new HashMap<>();
        result.put("PASS", passedEvaluations);
        result.put("FAIL", failedEvaluations);

        return result;
    }


    private InterviewEvaluationCompleteResponse mapToEvaluationResponse(Interview interview) {
        // 지원자 정보 가져오기
        Application application = interview.getApplication();
        User user = application.getUser();

        // 그룹명 가져오기
        List<InterviewEvaluator> evaluators = interviewEvaluatorRepository.findByInterview(interview);

        if (evaluators.isEmpty()) {
            throw new RuntimeException("InterviewEvaluator not found for interview");
        }

        InterviewEvaluator evaluator = evaluators.get(0);

        Group group = evaluator.getGroup();
        if (group == null) {
            throw new RuntimeException("Group not found for InterviewEvaluator");
        }

        return new InterviewEvaluationCompleteResponse(
                interview.getState().name(),
                user.getName(),
                user.getPhone(),
                group.getName()
        );
    }

    @Transactional
    public void completeInterviewEvaluation(Long recruitId, List<InterviewEvaluationCompleteRequest> evaluations, CustomUserDetails currentUser) {
        // 1. 리크루팅이 존재하는지 확인
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new IllegalArgumentException("리크루팅이 존재하지 않습니다."));

        // 2. 각 면접 평가 처리
        for (InterviewEvaluationCompleteRequest evaluation : evaluations) {
            Interview interview = interviewRepository.findById(evaluation.getInterviewId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 면접을 찾을 수 없습니다."));

            // 3. 면접 상태 업데이트
            EvaluateStatus status = EvaluateStatus.valueOf(evaluation.getState().toUpperCase());
            interview.setState(status);
            interviewRepository.save(interview); // 면접 상태 저장

            // 4. 지원서 상태 업데이트
            Application application = interview.getApplication();
            application.setState(status);
            applicationRepository.save(application); // 지원서 상태 저장
        }

        // 5. 리크루팅의 currentStage를 FINAL_PASS로 업데이트
        recruit.setCurrentStage(CurrentStage.FINAL_PASS);
        recruitRepository.save(recruit); // 리크루팅 상태 저장
    }

    public EvaluateUserResponse completeEachInterviewEvaluation(Long recruitId, InterviewEvaluationCompleteRequest request) {
        // 면접 ID를 통해 면접 정보 가져오기
        Interview interview = interviewRepository.findById(request.getInterviewId())
                .orElseThrow(() -> new IllegalArgumentException("해당 면접을 찾을 수 없습니다."));

        // 면접 평가 결과 업데이트 (PASS 또는 FAIL)
        if ("PASS".equalsIgnoreCase(request.getState())) {
            interview.setState(EvaluateStatus.PASS);
        } else if ("FAIL".equalsIgnoreCase(request.getState())) {
            interview.setState(EvaluateStatus.FAIL);
        } else {
            throw new IllegalArgumentException("결과 값은 'PASS' 또는 'FAIL'이어야 합니다.");
        }
        interviewRepository.save(interview); // 면접 상태 저장

        // 해당 면접을 진행한 지원자 정보 가져오기
        Application application = interview.getApplication();
        User user = application.getUser();

        // 그룹명 가져오기 (InterviewEvaluator를 통해)
        InterviewEvaluator evaluator = interviewEvaluatorRepository.findFirstByInterviewId(request.getInterviewId())
                .orElseThrow(() -> new IllegalArgumentException("해당 면접의 평가자가 없습니다."));
        Group group = evaluator.getGroup();

        // 응답 객체 생성
        return new EvaluateUserResponse(
                user.getName(),
                user.getPhone(),
                group != null ? group.getName() : null,
                request.getState()
        );
    }

    public void updateStateToObjection(Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 면접을 찾을 수 없습니다."));
        interview.setState(EvaluateStatus.OBJECTION);
        interviewRepository.save(interview);
    }

    private static String getString(List<DocumentEvaluator> evaluators) {
        String groupName = null;
        if(evaluators.get(0).getGroup().isCommon()){ //공통그룹이면
            for (DocumentEvaluator evaluator : evaluators){
                if (evaluator.getGroup().getEvalType().toString().equals("DOCUMENT")){
                    groupName = evaluators.get(0).getGroup().getName();
                    System.out.println("@@----\n\n그룹명 = "+evaluator.getGroup().getEvalType()+"\n\n");
                }
                else{
                    groupName = "";
                }
            }
        }
        else {
            groupName = evaluators.get(0).getGroup().getName(); // 첫 번째 평가자의 그룹 사용
        }
        return groupName;
    }

    public EachInterviewEvaluationResponse getInterviewEvaluation(Long recruitId, Long interviewId, CustomUserDetails currentUser) {
        // 1. 지원자 정보
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new DocumentEvaluationService.ResourceNotFoundException("Interview not found"));
        Application application = interview.getApplication();
        User user = application.getUser();

        if (user == null) {
            throw new DocumentEvaluationService.ResourceNotFoundException("User not found");
        }

        List<InterviewEvaluator> evaluators = interviewEvaluatorRepository.findByInterviewId(interviewId);
        String groupName = (evaluators.isEmpty() || evaluators.get(0).getGroup() == null) ? null : evaluators.get(0).getGroup().getName();

        ApplicantInfo applicantInfo = ApplicantInfo.of(
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getLocation(),
                user.getProfile(),
                user.getSchool(),
                user.getMajor(),
                user.getDoubleMajor(),
                user.getSemester() != null ? String.valueOf(user.getSemester()) : null, // Null check for semester
                groupName
        );

        // 2. 면접 질문 및 답변
        List<InterviewQuestion> interviewQuestions = interviewQuestionRepository.findByInterviewId(interviewId);
        Map<QuestionType2, List<InterviewQA>> groupedQuestions = new HashMap<>();
        for (QuestionType2 type : QuestionType2.values()) {
            groupedQuestions.put(type, new ArrayList<>());
        }

        for (InterviewQuestion question : interviewQuestions) {
            InterviewAnswer answer = question.getInterviewAnswer();
            groupedQuestions.get(question.getType()).add(InterviewQA.of(
                    question.getContent(),
                    answer != null ? answer.getContent() : null
            ));
        }

        // 3. 인재상
        List<Ideal> ideals = idealRepository.findByGroupId(evaluators.isEmpty() || evaluators.get(0).getGroup() == null ? null : evaluators.get(0).getGroup().getId());
        List<String> idealDetails = ideals.stream()
                .map(Ideal::getContent)
                .collect(Collectors.toList());

        // 4. 총점 평균
        Integer averageScore = interview.getScore();

        // 5. 다른 운영진 평가 보기
        List<InterviewEvaluatorScores> evaluatorScores = evaluators.stream()
                .map(evaluator -> InterviewEvaluatorScores.of(evaluator, interviewScoreRepository))
                .collect(Collectors.toList());

        // 6. 내 평가 보기
        Long currentClubUserId = currentUser.getUser() != null ? currentUser.getUser().getId() : null;
        if (currentClubUserId == null) {
            throw new DocumentEvaluationService.ResourceNotFoundException("Current Club User not found");
        }

        ClubUser currentClubUser = clubUserRepository.findById(currentClubUserId)
                .orElseThrow(() -> new DocumentEvaluationService.ResourceNotFoundException("Club User not found"));

        InterviewEvaluatorScores myEvaluation = evaluators.stream()
                .filter(evaluator -> evaluator.getClubUser() != null && evaluator.getClubUser().getId().equals(currentClubUserId))
                .findFirst()
                .map(evaluator -> InterviewEvaluatorScores.of(evaluator, interviewScoreRepository))
                .orElse(null);

        return new EachInterviewEvaluationResponse(
                applicantInfo,
                groupedQuestions,
                idealDetails,
                averageScore,
                evaluatorScores,
                myEvaluation
        );
    }

    @Transactional
    public InterviewEvaluationResponseDto evaluateInterview(Long interviewId, Long clubUserId, InterviewEvaluationRequestDto request) {
        // 면접 평가자 찾기
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid interview ID"));

        // 면접 평가자(InterviewEvaluator) 찾기
        InterviewEvaluator evaluator = interviewEvaluatorRepository.findByInterviewIdAndClubUserId(interviewId, clubUserId)
                .orElseThrow(() -> new IllegalArgumentException("Evaluator not found"));

        // 평가 기준 처리
        int totalScore = 0;
        for (InterviewEvaluationRequestDto.CriteriaEvaluation criteriaEvaluation : request.getCriteriaEvaluations()) {
            InterviewCriteria criteria = interviewCriteriaRepository.findById(criteriaEvaluation.getCriteriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid criteria ID"));

            InterviewScore score = new InterviewScore();
            score.setInterviewCriteria(criteria);
            score.setInterviewEvaluator(evaluator);
            score.setScore(criteriaEvaluation.getScore());
            totalScore += criteriaEvaluation.getScore();

            interviewScoreRepository.save(score);
        }

        // 평가자의 총 점수 업데이트
        evaluator.setScore(totalScore);
        evaluator.setComment(request.getComment());
        interviewEvaluatorRepository.save(evaluator);

        // 면접 엔티티의 numClubUser 업데이트 (null이면 1로 설정, 아니면 증가)
        if (interview.getNumClubUser() == null) {
            interview.setNumClubUser(1);  // 처음 평가하는 경우 numClubUser가 null일 수 있으므로 1로 설정
        } else {
            interview.setNumClubUser(interview.getNumClubUser() + 1);  // 기존 값에 1을 더함
        }

        // 새로운 평균 점수 계산
        int newAverageScore = interview.getScore() == null ?
                totalScore : (interview.getScore() * (interview.getNumClubUser() - 1) + totalScore) / interview.getNumClubUser();
        interview.setScore(newAverageScore);

        // 면접 상태를 'EDITABLE'로 업데이트
        evaluator.setStage(Stage.EDITABLE);
        interviewEvaluatorRepository.save(evaluator);
        interviewRepository.save(interview);

        // 응답 생성
        return new InterviewEvaluationResponseDto(interviewId, totalScore, request.getComment(), "UPDATED");
    }

    public List<InterviewResponseDTO> getInterviewScheduleByRecruitId(Long recruitId) {
        // Step 1: Application 엔티티 조회
        List<Application> applications = applicationRepository.findAllByRecruitId(recruitId);
        List<Long> applicationIds = applications.stream()
                .map(Application::getId)
                .filter(Objects::nonNull)  // Null 체크 추가
                .collect(Collectors.toList());

        // Step 2: Interview 엔티티 조회
        List<Interview> interviews = interviewRepository.findAllByApplicationIdIn(applicationIds);
        List<Long> interviewIds = interviews.stream()
                .map(Interview::getId)
                .filter(Objects::nonNull)  // Null 체크 추가
                .collect(Collectors.toList());

        // Step 3: InterviewEvaluator 엔티티 조회
        List<InterviewEvaluator> evaluators = interviewEvaluatorRepository.findAllByInterviewIdIn(interviewIds);

        // Step 4: 날짜 및 시간대 오름차순 정렬
        List<InterviewEvaluator> sortedEvaluators = evaluators.stream()
                .filter(e -> e.getInterviewTime() != null)  // InterviewTime이 null인 경우 제외
                .sorted(Comparator.comparing(e -> e.getInterviewTime())) // 날짜 및 시간대 기준 정렬
                .collect(Collectors.toList());

        // Step 5: 날짜별, 시간대별로 데이터 그룹화
        Map<LocalDate, Map<LocalTime, List<InterviewEvaluator>>> groupedData = sortedEvaluators.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getInterviewTime().toLocalDate(),
                        Collectors.groupingBy(e -> e.getInterviewTime().toLocalTime())
                ));

        // Step 6: DTO 생성
        List<InterviewResponseDTO> response = groupedData.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // 날짜 오름차순 정렬
                .map(dateEntry -> {
                    LocalDate date = dateEntry.getKey();
                    Map<LocalTime, List<InterviewEvaluator>> timeData = dateEntry.getValue();

                    List<InterviewResponseDTO.TimeSlotDTO> timeSlots = timeData.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey()) // 시간대 오름차순 정렬
                            .map(timeEntry -> {
                                LocalTime time = timeEntry.getKey();
                                List<InterviewEvaluator> evaluatorsAtTime = timeEntry.getValue();

                                // 그룹별 데이터 분리
                                Map<Long, List<InterviewEvaluator>> groupedByGroup = evaluatorsAtTime.stream()
                                        .filter(evaluator -> evaluator.getGroup() != null)  // 그룹이 null인 경우 제외
                                        .collect(Collectors.groupingBy(evaluator -> evaluator.getGroup().getId()));

                                List<InterviewResponseDTO.TimeSlotDTO.GroupDTO> groups = groupedByGroup.entrySet().stream()
                                        .map(groupEntry -> {
                                            Long groupId = groupEntry.getKey();
                                            List<InterviewEvaluator> groupEvaluators = groupEntry.getValue();

                                            // 면접관 이름 리스트
                                            List<Long> clubUserIds = groupEvaluators.stream()
                                                    .filter(evaluator -> evaluator.getClubUser() != null)  // ClubUser가 null인 경우 제외
                                                    .map(evaluator -> evaluator.getClubUser().getId())
                                                    .distinct()
                                                    .collect(Collectors.toList());

                                            List<String> evaluatorNames = clubUserRepository.findAllByIdIn(clubUserIds).stream()
                                                    .map(clubUser -> clubUser.getUser() != null ? clubUser.getUser().getName() : null)  // User가 null인 경우 처리
                                                    .filter(Objects::nonNull)  // Null 제외
                                                    .collect(Collectors.toList());

                                            // 면접자 이름 리스트
                                            List<Long> applicantIds = groupEvaluators.stream()
                                                    .filter(evaluator -> evaluator.getInterview() != null && evaluator.getInterview().getApplication() != null && evaluator.getInterview().getApplication().getUser() != null)
                                                    .map(e -> e.getInterview().getApplication().getUser().getId())
                                                    .distinct()
                                                    .collect(Collectors.toList());

                                            List<String> applicantNames = userRepository.findAllByIdIn(applicantIds).stream()
                                                    .map(user -> user.getName())
                                                    .collect(Collectors.toList());

                                            return InterviewResponseDTO.TimeSlotDTO.GroupDTO.builder()
                                                    .groupName(groupEvaluators.get(0).getGroup() != null ? groupEvaluators.get(0).getGroup().getName() : null) // 그룹 이름 null 처리
                                                    .interviewer(evaluatorNames)
                                                    .interviewee(applicantNames)
                                                    .build();
                                        }).collect(Collectors.toList());

                                return InterviewResponseDTO.TimeSlotDTO.builder()
                                        .time(time != null ? time.toString() : null) // 시간대 null 처리
                                        .groups(groups)
                                        .build();
                            }).collect(Collectors.toList());

                    return InterviewResponseDTO.builder()
                            .date(date != null ? date.toString() : null)  // 날짜 null 처리
                            .timeSlots(timeSlots)
                            .build();
                }).collect(Collectors.toList());

        return response;
    }

    // 파트 존재 여부 조회
    @Transactional(readOnly = true)
    public Boolean isCommon(Long recruitId) {
        return groupRepository.findByRecruitId(recruitId).get(0).isCommon();
    }

    // 면접 평가 준비하기
    @Transactional(readOnly = true)
    public LoadDocumentSettingResponseDto findDocSetting(Long recruitId) {
        Map<String, LoadDocumentSettingResponseDto.Participant> result = new HashMap<>();

        List<Group> groups = groupRepository.findByRecruitId(recruitId);
        for (Group group : groups) {
            List<DocumentEvaluator> documentEvaluators = documentEvaluatorRepository.findAllByGroup_Id(group.getId());
            Set<Long> staffIds = new HashSet<>();
            Set<Long> applicantIds = new HashSet<>();

            List<ClubUserResponseDto> staff = new ArrayList<>();
            List<UserResponseDto> applicant = new ArrayList<>();

            for (DocumentEvaluator evaluator : documentEvaluators) {
                if (staffIds.add(evaluator.getClubUser().getId())) {
                    staff.add(ClubUserResponseDto.toDto(evaluator.getClubUser()));
                }

                if (applicantIds.add(evaluator.getApplication().getUser().getId())) {
                    if (evaluator.getApplication().getState().equals(EvaluateStatus.PASS)) {
                        applicant.add(UserResponseDto.toDto(evaluator.getApplication().getUser()));
                    }
                }
            }

            LoadDocumentSettingResponseDto.Participant participant = LoadDocumentSettingResponseDto.Participant.builder().staff(staff).applicant(applicant).build();
            result.put(group.getName(), participant);
        }

        return LoadDocumentSettingResponseDto.builder().group(result).build();
    }

    // 서류 합격자 수 조회
    @Transactional(readOnly = true)
    public RecruitNumResponseDto findDocRecruit(Long recruitId) {
        Map<String, Integer> groupMap = new HashMap<>();
        List<Group> groups = groupRepository.findByRecruitId(recruitId);
        int totalNum = 0;
        for (Group group : groups) {
            totalNum += group.getNumDoc();
            if (!group.isCommon()) {
                groupMap.put(group.getName(), group.getNumDoc());
            }
        }

        return new RecruitNumResponseDto(totalNum, groupMap);
    }

    // 서류 합격자들 모두 조회하기
    @Transactional(readOnly = true)
    public List<InterviewPrepResponseDto> findApplicants(Long recruitId) {
        return applicationRepository.findByRecruitId(recruitId)
                .stream()
                .filter(application -> application.getState() == EvaluateStatus.PASS)
                .map(InterviewPrepResponseDto::toDto)
                .toList();
    }

    // 면접자들에 대한 면접 질문 저장
    @Transactional
    public void saveInterviewQuestions(Long recruitId, InterviewQuestionSaveRequestDto interviewQuestionSaveRequestDto) {
        Recruit recruit = recruitRepository.findById(recruitId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 모집공고 입니다.")
        );

        // 1. 그룹 확인 및 생성
        List<Group> groups = groupRepository.findByRecruitId(recruitId);
        Group group = groups.get(0);

        if (group.isCommon()) { // 공통 그룹일 경우
            for (InterviewQuestionSaveRequestDto.InterviewStaffAllocate allocate : interviewQuestionSaveRequestDto.getAllocates()) {
                groupRepository.save(Group.of(recruit, allocate.getGroupName(), group.getNumDoc(), group.getNumFinal(), group.getNumRecruit(), group.getWarning(), EvalType.INTERVIEW, true));
            }
        } else { // 개별 그룹일 경우
            for (Group docGroup : groups) {
                groupRepository.save(Group.of(recruit, docGroup.getName(), docGroup.getNumDoc(), docGroup.getNumFinal(), docGroup.getNumRecruit(), docGroup.getWarning(), EvalType.INTERVIEW, false));
            }
        }

        // 2. 서류 합격자 필터링 및 면접 생성
        List<Application> applications = applicationRepository.findByRecruitId(recruitId).stream()
                .filter(application -> application.getState() == EvaluateStatus.PASS)
                .toList();

        List<Interview> interviews = new ArrayList<>();
        for (Application application : applications) {
            interviews.add(interviewRepository.save(Interview.of(application)));
        }

        // 3. INTERVIEW 그룹 필터링
        groups = groupRepository.findByRecruitId(recruitId)
                .stream()
                .filter(g -> g.getEvalType() == EvalType.INTERVIEW)
                .toList();

        // 4. InterviewEvaluator 생성 및 저장
        List<InterviewEvaluator> interviewEvaluators = new ArrayList<>();
        for (InterviewQuestionSaveRequestDto.InterviewStaffAllocate allocate : interviewQuestionSaveRequestDto.getAllocates()) {
            Group assignedGroup = groups.stream()
                    .filter(g -> g.getName().equals(allocate.getGroupName()))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "그룹을 찾을 수 없습니다: " + allocate.getGroupName()));

            for (int i = 0; i < allocate.getStaff().size(); i++) {
                Long staffId = allocate.getStaff().get(i);
                ClubUser clubUser = findClubUser(staffId);
                Interview interview = interviews.get(i % interviews.size());

                InterviewEvaluator interviewEvaluator = InterviewEvaluator.of(clubUser, interview, assignedGroup);
                interviewEvaluators.add(interviewEvaluatorRepository.save(interviewEvaluator));
            }
        }

        // 5. 공통 질문 저장
        if (interviewQuestionSaveRequestDto.getCommon() != null) {
            for (String question : interviewQuestionSaveRequestDto.getCommon()) {
                interviewQuestionRepository.save(
                        InterviewQuestion.of(null, null, question, QuestionType2.COMMON) // 평가자와 면접 없음
                );
            }
        }

        // 6. 그룹 질문 저장
        if (interviewQuestionSaveRequestDto.getGroup() != null) {
            for (InterviewQuestionSaveRequestDto.InterviewGroupQuestion groupQuestion : interviewQuestionSaveRequestDto.getGroup()) {
                Group targetGroup = groups.stream()
                        .filter(g -> g.getName().equals(groupQuestion.getGroupName()))
                        .findFirst()
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "그룹을 찾을 수 없습니다: " + groupQuestion.getGroupName()));

                for (String question : groupQuestion.getQuestion()) {
                    // Group에 해당하는 InterviewEvaluator 찾기
                    InterviewEvaluator evaluator = interviewEvaluatorRepository.findByGroupId(targetGroup.getId());

                    interviewQuestionRepository.save(
                            InterviewQuestion.of(evaluator, null, question, QuestionType2.COMMON)
                    );
                }
            }
        }

        // 7. 면접 평가 기준 저장
        Map<String, InterviewQuestionSaveRequestDto.InterviewEvaluateCriteria> criteriaMap = interviewQuestionSaveRequestDto.getCriteria();
        for (Group g : groups) {
            for (String groupName : criteriaMap.keySet()) {
                if (g.getName().equals(groupName)) {
                    InterviewEvaluator evaluator = interviewEvaluatorRepository.findByGroupId(g.getId());
                    InterviewQuestionSaveRequestDto.InterviewEvaluateCriteria c = criteriaMap.get(groupName);
                    interviewCriteriaRepository.save(
                            InterviewCriteria.of(evaluator, c.getName(), c.getContent(), c.getScore())
                    );
                }
            }
        }
    }

    @Transactional
    public void saveIndividualQuestion(Long recruitId, Long userId, InterviewIndividualQuestionRequestDto interviewIndividualQuestionRequestDto) {
        Interview interview = interviewRepository.findByUser_IdAndRecruit_Id(recruitId, userId)
                .orElseThrow(() -> new IllegalArgumentException("면접을 찾을 수 없음"));
        List<InterviewEvaluator> interviewEvaluators = interviewEvaluatorRepository.findByInterviewId(interview.getId());

        for (String question : interviewIndividualQuestionRequestDto.getQuestion()) {
            for (InterviewEvaluator interviewEvaluator : interviewEvaluators) {
                interviewQuestionRepository.save(
                        InterviewQuestion.of(interviewEvaluator, interview, question, QuestionType2.PERSONAL)
                );
            }
        }
    }

    // 면접 합격자 리스트
    public void interviewResult(Long recruitId, SortType sortType) {
    }

    public ClubUser findClubUser(Long clubUserId) {
        return clubUserRepository.findById(clubUserId).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "존재하지 않는 운영진 입니다."
                )
        );
    }
}
