package com.cluting.clutingbackend.evaluation.controller;

import com.cluting.clutingbackend.evaluation.dto.response.EvaluationResponse;
import com.cluting.clutingbackend.evaluation.service.TempService;
import com.cluting.clutingbackend.global.enums.EvaluateStatus;
import com.cluting.clutingbackend.global.enums.Stage;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "[ 평가 전/중/후/완료 ] 지원서 리스트 가져오기")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/app-list/{recruitId}")
public class TempController {

    private final TempService tempService;

    @Operation(summary = "[서류] 평가 전 지원서 리스트", description = "현재 로그인한 유저가 평가 전인 서류들을 반환합니다.")
    @GetMapping("/document/before")
    public List<EvaluationResponse> getBeforeEvaluations(
            @PathVariable Long recruitId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return tempService.getEvaluationsByStage(recruitId, currentUser, "BEFORE");
    }

    @Operation(summary = "[서류] 평가 중 지원서 리스트", description = "현재 로그인한 유저가 평가 중인 서류와 본인은 평가 완료했지만 팀원들이 평가 중인 서류를 반환합니다.")
    @GetMapping("/document/ing")
    public List<EvaluationResponse> getInProgressEvaluations(
            @PathVariable Long recruitId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return tempService.getEvaluationsByStage(recruitId, currentUser, "ING");
    }

    @Operation(summary = "[서류] 평가 후 지원서 리스트", description = "모든 운영진이 평가를 완료한 서류를 반환합니다.")
    @GetMapping("/document/after")
    public List<EvaluationResponse> getAfterEvaluations(
            @PathVariable Long recruitId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return tempService.getEvaluationsByStage(recruitId, currentUser, "AFTER");
    }

    @Operation(summary = "[서류] 평가 완료 지원서 리스트", description = "합격/불합격이 결정된 서류를 반환합니다.")
    @GetMapping("/document/complete")
    public List<EvaluationResponse> getCompleteEvaluations(
            @PathVariable Long recruitId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return tempService.getEvaluationsByStage(recruitId, currentUser, "COMPLETE");
    }

    @PatchMapping("/document/evaluate-status/{applicationId}")
    @Operation(
            summary = "[서류] 지원서 상태 업데이트 하기",
            description = "지원서의 상태를 이의제기/이의반영/합격/불합격으로 변경시키는 API입니다."
    )
    public ResponseEntity<String> updateEvaluateStatus(
            @PathVariable Long applicationId,
            @RequestParam EvaluateStatus newStatus) {
        tempService.updateEvaluateStatus(applicationId, newStatus);
        return ResponseEntity.ok("성공적으로 지원서 상태가 변경되었습니다. ");
    }

///////////////////////////////////////////////////////////////////////////////////////////
@Operation(summary = "[면접] 평가 전 지원자 정보 불러오기",
        description = "평가 전 단계의 지원자 정보를 가져옵니다. 정렬: newest = 최신순, oldest = 지원순")
@GetMapping("/before")
public List<EvaluationResponse> getBeforeEvaluations(
        @PathVariable Long recruitId,
        @AuthenticationPrincipal CustomUserDetails currentUser,
        @RequestParam(required = false) String groupName,
        @RequestParam(required = false) String sortOrder) {

    return tempService.getEvaluationsByStage(recruitId, currentUser, groupName, sortOrder, Stage.BEFORE);
}

    @Operation(summary = "[면접] 평가 중 지원자 정보 불러오기",
            description = "평가 중 단계의 지원자 정보를 가져옵니다. 정렬: newest = 최신순, oldest = 지원순")
    @GetMapping("/in-progress")
    public List<EvaluationResponse> getInProgressEvaluations(
            @PathVariable Long recruitId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(required = false) String groupName,
            @RequestParam(required = false) String sortOrder) {

        return tempService.getEvaluationsByStage(recruitId, currentUser, groupName, sortOrder, Stage.ING);
    }

    @Operation(summary = "[면접] 평가 후 지원자 정보 불러오기",
            description = "평가 후 단계의 지원자 정보를 가져옵니다. 정렬: newest = 최신순, oldest = 지원순")
    @GetMapping("/after")
    public List<EvaluationResponse> getAfterEvaluations(
            @PathVariable Long recruitId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(required = false) String groupName,
            @RequestParam(required = false) String sortOrder) {

        return tempService.getEvaluationsByStage(recruitId, currentUser, groupName, sortOrder, Stage.AFTER);
    }


}
