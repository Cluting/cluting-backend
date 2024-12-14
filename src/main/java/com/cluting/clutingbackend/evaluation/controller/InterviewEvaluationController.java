package com.cluting.clutingbackend.evaluation.controller;

import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.evaluation.dto.GroupResponse;
import com.cluting.clutingbackend.evaluation.dto.interview.*;
import com.cluting.clutingbackend.evaluation.service.InterviewEvaluationService;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.interview.repository.InterviewRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "[면접 평가하기] 평가전/중/후 지원자 정보 불러오기",
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

    @Operation(summary = "[면접 평가하기-필터링 용] 그룹명 가져오기")
    @GetMapping("/groups")
    public ResponseEntity<List<GroupResponse>> getGroupsByRecruitId(@RequestParam Long recruitId) {
        List<GroupResponse> groups = interviewEvaluationService.getGroupsByRecruitId(recruitId);
        return ResponseEntity.ok(groups);
    }

    @Operation(summary = "[면접 평가하기] 평가 후 완료하기")
    @PostMapping
    public ResponseEntity<List<InterviewEvaluationResultDto>> evaluateInterviews(
            @PathVariable Long recruitId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        List<InterviewEvaluationResultDto> results = interviewEvaluationService.evaluateInterviews(recruitId, currentUser);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "[면접 평가하기] 평가 완료 불러오기")
    @GetMapping("/complete")
    public Map<String, List<InterviewEvaluationCompleteResponse>> getCompletedEvaluations(
            @PathVariable Long recruitId) {
        return interviewEvaluationService.getCompletedEvaluations(recruitId);
    }

    @Operation(summary = "[면접 평가하기] 평가 완료 전송하기",
            description = "면접 평가가 완료되면 모든 면접의 상태를 PASS 또는 FAIL로 업데이트하고, 해당 리크루팅 단계도 FINAL_PASS로 변경합니다.")
    @PostMapping("/complete")
    public ResponseEntity<String> completeInterviewEvaluation(
            @PathVariable Long recruitId,
            @RequestBody List<InterviewEvaluationCompleteRequest> evaluations,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        interviewEvaluationService.completeInterviewEvaluation(recruitId, evaluations, currentUser);

        return ResponseEntity.ok("면접 평가가 완료되었습니다.");
    }

    @Operation(summary = "[면접 평가하기] 면접 평가 상태 업데이트",
            description = "면접 평가 결과를 합격(PASS) 또는 불합격(FAIL)으로 변경하고, 해당 면접을 한 지원자 정보를 반환합니다.")
    @PostMapping("/each")
    public ResponseEntity<EvaluateUserResponse> completeInterviewEvaluation(
            @PathVariable Long recruitId,
            @RequestBody InterviewEvaluationCompleteRequest request) {

        EvaluateUserResponse response = interviewEvaluationService.completeEachInterviewEvaluation(recruitId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{interviewId}/state")
    public ResponseEntity<Void> updateStateToObjection(@PathVariable Long interviewId) {
        interviewEvaluationService.updateStateToObjection(interviewId);
        return ResponseEntity.noContent().build();
    }
}