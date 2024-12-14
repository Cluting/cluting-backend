package com.cluting.clutingbackend.evaluation.controller;

import com.cluting.clutingbackend.evaluation.dto.GroupResponse;
import com.cluting.clutingbackend.evaluation.dto.interview.InterviewEvaluationRequest;
import com.cluting.clutingbackend.evaluation.dto.interview.InterviewEvaluationResponse;
import com.cluting.clutingbackend.evaluation.service.InterviewEvaluationService;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@Tag(name = "[5. 면접 평가하기]", description = "면접 평가 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/eval/interview/{recruitId}")
public class InterviewEvaluationController {

    private final InterviewEvaluationService interviewEvaluationService;

    @Operation(summary = "[면접 평가하기] 지원자 정보 불러오기",
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

    @Operation(summary = "[면접 평가하기] 그룹명 가져오기")
    @GetMapping("/groups")
    public ResponseEntity<List<GroupResponse>> getGroupsByRecruitId(@RequestParam Long recruitId) {
        List<GroupResponse> groups = interviewEvaluationService.getGroupsByRecruitId(recruitId);
        return ResponseEntity.ok(groups);
    }
}