package com.cluting.clutingbackend.evaluation.controller;

import com.cluting.clutingbackend.evaluation.dto.request.InterviewIndividualQuestionRequestDto;
import com.cluting.clutingbackend.evaluation.dto.request.InterviewQuestionSaveRequestDto;
import com.cluting.clutingbackend.evaluation.dto.request.MessageSendRequestDto;
import com.cluting.clutingbackend.evaluation.dto.request.ScheduleFormDataRequestDto;
import com.cluting.clutingbackend.evaluation.dto.response.*;
import com.cluting.clutingbackend.global.enums.EvaluateStatus;
import com.cluting.clutingbackend.global.enums.SortType;
import com.cluting.clutingbackend.recruit.dto.response.RecruitNumResponseDto;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.cluting.clutingbackend.evaluation.dto.GroupResponse;
import com.cluting.clutingbackend.evaluation.dto.interview.*;
import com.cluting.clutingbackend.evaluation.service.InterviewEvaluationService;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.Map;

@Tag(name = "[5. 면접 평가하기]", description = "면접 평가 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/eval/interview/{recruitId}")
public class InterviewEvaluationController {

    private final InterviewEvaluationService interviewEvaluationService;

    @Operation(summary = "[면접 합격자 및 면접 안내] 6-2. 면접 일정 조정하기 (면접 가능 일정 조회)",
            description = "면접관/지원자들의 면접 가능 일정을 조회합니다.")
    @GetMapping("/avail")
    public InterviewAvailScheduleResponseDto findSchedules(
            @PathVariable Long recruitId) {
        return interviewEvaluationService.findSchedules(recruitId);
    }

    @Operation(summary = "[면접 합격자 및 면접 안내] 6-2. 면접 일정 조정하기 (입력 저장)",
            description = "면접 일정을 확정합니다.")
    @PostMapping("/set")
    public ResponseEntity<Void> saveInterviewSchedule(
            @PathVariable Long recruitId,
            @RequestBody ScheduleFormDataRequestDto scheduleFormDataRequestDto) {
        interviewEvaluationService.saveInterviewSchedule(recruitId, scheduleFormDataRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "[면접 합격자 및 면접 안내] 6-3. 메시지 조회하기",
            description = "저장된 메시지를 조회합니다.")
    @GetMapping("/msg")
    public MessageResponseDto send(
            @PathVariable Long recruitId) {
        return interviewEvaluationService.findMessage(recruitId);
    }

    @Operation(summary = "[최종합격자 및 활동 안내] 6-2. 면접 합격자/불합격자 리스트 조회하기",
            description = "입력 받는 state(PASS 또는 FAIL)에 따라 면접 합격자 또는 불합격자 리스트를 조회합니다.")
    @GetMapping("/result/each")
    public List<InterviewResultListResponseDto> getList(
            @PathVariable Long recruitId,
            @RequestParam("state") EvaluateStatus status) {
        return interviewEvaluationService.getList(recruitId, status);
    }

    @Operation(summary = "[최종합격자 및 활동 안내] 6-2. 합불 안내 메시지 전송하기",
            description = "입력 받는 전화번호로 입력 받은 메시지를 전송합니다.")
    @PostMapping("/send")
    public ResponseEntity<Void> send(
            @PathVariable Long recruitId,
            @RequestBody MessageSendRequestDto messageSendRequestDto,
            @RequestParam("state") EvaluateStatus status) {
        interviewEvaluationService.send(recruitId, messageSendRequestDto, status);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "[면접 평가하기] 5-1. <평가 전> 그룹 조회",
            description = "그룹 조회하기")
    @GetMapping("/prep/group")
    public LoadDocumentSettingResponseDto findDocSetting(
            @PathVariable Long recruitId) {
        return interviewEvaluationService.findDocSetting(recruitId);
    }

    @Operation(summary = "[면접 평가하기] 5-1. <평가 전> 면접 평가 준비하기",
            description = "면접 평가 준비하기(저장)")
    @PostMapping("/prep")
    public ResponseEntity<Void> updateStagesToAfter(
            @PathVariable Long recruitId,
            @RequestBody InterviewQuestionSaveRequestDto interviewQuestionSaveRequestDto) {
        interviewEvaluationService.saveInterviewQuestions(recruitId, interviewQuestionSaveRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "[면접 평가하기] 5-1. <평가 전> 면접 평가 준비하기",
            description = "개인 질문 저장")
    @PostMapping("/prep/{userId}")
    public ResponseEntity<Void> updateStagesToAfter(
            @PathVariable("recruitId") Long recruitId,
            @PathVariable("userId") Long userId,
            @RequestBody InterviewIndividualQuestionRequestDto interviewIndividualQuestionRequestDto) {
        interviewEvaluationService.saveIndividualQuestion(recruitId, userId, interviewIndividualQuestionRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "[최종합격자 및 활동 안내] 6-1. <지원자 합불 결과>",
            description = "면접 합격자, 불합격자 리스트를 반환합니다. sort: NEWEST(최신순) OLDEST(오래된순) INORDER(가나다순)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "면접 합격자, 불합격자 리스트 반환 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping("/result")
    public InterviewEvaluationResultResponseDto findInterviewPassAndFail(
            @PathVariable("recruitId") Long recruitId,
            @RequestParam("sort") SortType sortType) {
        return interviewEvaluationService.findInterviewPassAndFail(recruitId, sortType);
    }

    @Operation(
            summary = "[서류 합격자 및 면접 안내] 4-2. <면접 가능 일정 조회하기>",
            description = "운영진 및 지원자들의 면접 가능 일정을 조회합니다. part 입력 안하면 _공통_ 으로 처리합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "면접 가능 일정 리스트 반환 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping("/available")
    public InterviewClassifyResponseDto findInterviewAvailable(
            @PathVariable("recruitId") Long recruitId,
            @RequestParam(value = "part", defaultValue = "공통") String partName) {
        return interviewEvaluationService.findInterviewAvailable(recruitId, partName);
    }

    @Operation(summary = "평가전/중/후 지원자 정보 불러오기",
            description = "면접 평가 단계의 지원자 정보를 단계별로 분리하여 가져옵니다.+" +
                    "정렬 : newest = 최신순, oldest = 지원순")
    @GetMapping
    public List<InterviewEvaluationResponse> getInterviewEvaluations(
            @PathVariable Long recruitId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(required = false) String groupName,
            @RequestParam(required = false) String sortOrder) {

        InterviewEvaluationRequest request = new InterviewEvaluationRequest(groupName, sortOrder);
        return interviewEvaluationService.getInterviewEvaluations(recruitId, currentUser, request);
    }

    @Operation(summary = "[필터링 용] 그룹명 가져오기")
    @GetMapping("/groups")
    public ResponseEntity<List<GroupResponse>> getGroupsByRecruitId(@RequestParam Long recruitId) {
        List<GroupResponse> groups = interviewEvaluationService.getGroupsByRecruitId(recruitId);
        return ResponseEntity.ok(groups);
    }

    @Operation(summary = "[평가 후] 완료하기")
    @PostMapping
    public ResponseEntity<List<InterviewEvaluationResultDto>> evaluateInterviews(
            @PathVariable Long recruitId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        List<InterviewEvaluationResultDto> results = interviewEvaluationService.evaluateInterviews(recruitId, currentUser);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "[평가 완료] 불러오기")
    @GetMapping("/complete")
    public Map<String, List<InterviewEvaluationCompleteResponse>> getCompletedEvaluations(
            @PathVariable Long recruitId) {
        return interviewEvaluationService.getCompletedEvaluations(recruitId);
    }

    @Operation(summary = "[평가 완료] 전송하기",
            description = "면접 평가가 완료되면 모든 면접의 상태를 PASS 또는 FAIL로 업데이트하고, 해당 리크루팅 단계도 FINAL_PASS로 변경합니다.")
    @PostMapping("/complete")
    public ResponseEntity<String> completeInterviewEvaluation(
            @PathVariable Long recruitId,
            @RequestBody List<InterviewEvaluationCompleteRequest> evaluations,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        interviewEvaluationService.completeInterviewEvaluation(recruitId, evaluations, currentUser);

        return ResponseEntity.ok("면접 평가가 완료되었습니다.");
    }

    @Operation(summary = "합불 결정",
            description = "면접 평가 결과를 합격(PASS) 또는 불합격(FAIL)으로 변경하고, 해당 면접을 한 지원자 정보를 반환합니다.")
    @PostMapping("/each")
    public ResponseEntity<EvaluateUserResponse> completeInterviewEvaluation(
            @PathVariable Long recruitId,
            @RequestBody InterviewEvaluationCompleteRequest request) {

        EvaluateUserResponse response = interviewEvaluationService.completeEachInterviewEvaluation(recruitId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "이의제기")
    @PatchMapping("/{interviewId}/state")
    public ResponseEntity<Void> updateStateToObjection(@PathVariable Long interviewId) {
        interviewEvaluationService.updateStateToObjection(interviewId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "[면접 평가하기] 불러오기", description = "지원자 정보 및 면접 질문, 답변, 기준별 점수와 코멘트를 가져옵니다.")
    @GetMapping("/{interviewId}/evaluate")
    public ResponseEntity<EachInterviewEvaluationResponse> getInterviewEvaluation(
            @PathVariable Long recruitId,
            @PathVariable Long interviewId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        EachInterviewEvaluationResponse response = interviewEvaluationService.getInterviewEvaluation(recruitId, interviewId, currentUser);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "[면접 평가하기] 전송하기", description = "면접에 대한 기준별 점수와 코멘트를 저장합니다.")
    @PostMapping("/{interviewId}/evaluate")
    public ResponseEntity<InterviewEvaluationResponseDto> evaluateInterview(
            @PathVariable Long interviewId,
            @Valid @RequestBody InterviewEvaluationRequestDto request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        Long currentClubUserId = currentUser.getUser().getId();  // 로그인한 사용자 ID
        InterviewEvaluationResponseDto response = interviewEvaluationService.evaluateInterview(interviewId, currentClubUserId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "면접 리스트", description = "날짜별 시간대별로 배정된 운영진, 지원자 리스트를 확인합니다.")
    @GetMapping("/list")
    public ResponseEntity<List<InterviewResponseDTO>> getInterviewSchedule(@PathVariable Long recruitId) {
        List<InterviewResponseDTO> schedule = interviewEvaluationService.getInterviewScheduleByRecruitId(recruitId);

        return ResponseEntity.ok(schedule);
    }
  
    @Operation(
            summary = "[면접 평가하기] 모집 그룹 공통/다수 여부 확인",
            description = "모집 그룹 공통/다수 여부를 확인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "모집 그룹 공통/다수 여부 확인 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping("/check")
    public Boolean isCommon(
            @PathVariable("recruitId") Long recruitId) {
        return interviewEvaluationService.isCommon(recruitId);
    }

    @Operation(
            summary = "[면접 평가하기] 이전에 설정한 서류 합격자 수 조회하기",
            description = "서류 합격자 수를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "서류 합격자 수 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping("/num")
    public RecruitNumResponseDto findDocRecruit(
            @PathVariable("recruitId") Long recruitId) {
        return interviewEvaluationService.findDocRecruit(recruitId);
    }

    @Operation(
            summary = "[면접 평가하기] 서류 합격자들 모두 조회하기",
            description = "서류 합격자들 모두 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "서류 합격자들 모두 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping("/prep")
    public List<InterviewPrepResponseDto> findApplicants(
            @PathVariable("recruitId") Long recruitId) {
        return interviewEvaluationService.findApplicants(recruitId);
    }
}
