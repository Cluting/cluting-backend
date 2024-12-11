package com.cluting.clutingbackend.evaluation.controller;

import com.cluting.clutingbackend.evaluation.dto.DocumentEvaluationRequest;
import com.cluting.clutingbackend.evaluation.dto.DocumentEvaluationResponse;
import com.cluting.clutingbackend.evaluation.dto.DocumentEvaluationWithStatusResponse;
import com.cluting.clutingbackend.evaluation.service.DocumentEvaluationService;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Tag(name = "[3. 서류 평가하기]", description = "서류 평가 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/eval/doc/{recruitId}")
public class DocumentEvaluationController {

    private final DocumentEvaluationService documentEvaluationService;

    // [서류 평가하기] 평가 전 상태 불러오기
    @Operation(summary = "[서류 평가하기] 평가 전 상태인 지원서 리스트 불러오기", description = "서류 평가 상태가 '평가 전'인 지원서들을 불러옵니다. 요청 시 필터링 조건으로 그룹명과 정렬 순서를 지정할 수 있습니다." +
            "\n\n" +
            "[그룹 필터링]" +
            "\n- 그룹 필터링을 하지 않으려면 null을 보내주세요." +
            "\n- 그룹 필터링을 원하면 그룹명을 지정해주세요." +
            "\n\n" +
            "[정렬 순서]" +
            "\n- 'newest' : 최신순 정렬 (생성일 기준 내림차순)" +
            "\n- 'oldest' : 오래된 순 정렬 (생성일 기준 오름차순)" +
            "\n- 정렬을 하지 않으려면 null을 보내주세요.")
    @PostMapping("/before")
    public List<DocumentEvaluationResponse> getPendingEvaluations(
            @PathVariable Long recruitId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestBody DocumentEvaluationRequest request) {
        return documentEvaluationService.getPendingEvaluations(recruitId, request, currentUser);
    }

    // [서류 평가하기] 평가 중 상태 불러오기
    @Operation(summary = "[서류 평가하기] 평가 중 상태인 지원서 리스트 불러오기", description = "서류 평가 상태가 '평가 중' 또는 '편집 가능'인 지원서들을 불러옵니다. 요청 시 필터링 조건으로 그룹명과 정렬 순서를 지정할 수 있습니다." +
            "\n\n" +
            "[그룹 필터링]" +
            "\n- 그룹 필터링을 하지 않으려면 null을 보내주세요." +
            "\n- 그룹 필터링을 원하면 그룹명을 지정해주세요." +
            "\n\n" +
            "[정렬 순서]" +
            "\n- 'newest' : 최신순 정렬 (생성일 기준 내림차순)" +
            "\n- 'oldest' : 오래된 순 정렬 (생성일 기준 오름차순)" +
            "\n- 정렬을 하지 않으려면 null을 보내주세요.")
    @PostMapping("/ing")
    public Map<String, List<DocumentEvaluationResponse>> getEvaluationsInProgressOrEditable(
            @PathVariable Long recruitId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestBody DocumentEvaluationRequest request) {

        // "ING"와 "EDITABLE" 상태의 리스트를 반환하는 Map
        Map<String, List<DocumentEvaluationResponse>> evaluations = documentEvaluationService.getEvaluationsInProgressOrEditable(recruitId, request, currentUser);

        // 반환값을 구분하여 "ING"와 "EDITABLE" 상태의 리스트를 구분
        return evaluations;
    }

    // [서류 평가하기] 평가 후 상태 불러오기
    @Operation(summary = "[서류 평가하기] 평가 후 상태인 지원서 리스트 불러오기",
            description = "서류 평가 상태가 '평가 후'인 지원서들을 불러옵니다. " +
                    "요청 시 필터링 조건으로 그룹명과 정렬 순서를 지정할 수 있습니다." +
                    "\n\n[그룹 필터링]" +
                    "\n- 그룹 필터링을 하지 않으려면 null을 보내주세요." +
                    "\n- 그룹 필터링을 원하면 그룹명을 지정해주세요." +
                    "\n\n[정렬 순서]" +
                    "\n- 'newest' : 최신순 정렬 (생성일 기준 내림차순)" +
                    "\n- 'oldest' : 오래된 순 정렬 (생성일 기준 오름차순)" +
                    "\n- 정렬을 하지 않으려면 null을 보내주세요.")
    @PostMapping("/after")
    public List<DocumentEvaluationResponse> getEvaluationsAfter(
            @PathVariable Long recruitId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestBody DocumentEvaluationRequest request) {

        return documentEvaluationService.getEvaluationsAfter(recruitId, request, currentUser);
    }

    // [서류 평가하기] 평가 후 완료하기
    @Operation(summary = "[서류 평가하기] 평가 후 완료하기 전송",
            description = "서류 평가 상태가 'READABLE' 또는 'EDITABLE'인 지원서들의 상태를 'AFTER'로 변경합니다.")
    @PostMapping("/after_to_complete")
    public ResponseEntity<Void> updateStagesToAfter(
            @PathVariable Long recruitId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        documentEvaluationService.updateStagesToAfter(recruitId, currentUser);
        return ResponseEntity.ok().build();
    }

    // [서류 평가하기] 평가 완료 불러오기
    @Operation(summary = "[서류 평가하기] 평가 완료된 지원서 리스트 불러오기",
            description = "서류 평가 상태가 '평가 완료'인 지원서들을 불러옵니다. " +
                    "요청 시 필터링 조건으로 그룹명과 정렬 순서를 지정할 수 있습니다." +
                    "\n\n[그룹 필터링]" +
                    "\n- 그룹 필터링을 하지 않으려면 null을 보내주세요." +
                    "\n- 그룹 필터링을 원하면 그룹명을 지정해주세요." +
                    "\n\n[정렬 순서]" +
                    "\n- 'newest' : 최신순 정렬 (생성일 기준 내림차순)" +
                    "\n- 'oldest' : 오래된 순 정렬 (생성일 기준 오름차순)" +
                    "\n- 정렬을 하지 않으려면 null을 보내주세요.")
    @PostMapping("/complete")
    public Map<String, List<DocumentEvaluationWithStatusResponse>> getCompletedEvaluations(
            @PathVariable Long recruitId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestBody DocumentEvaluationRequest request) {

        return documentEvaluationService.getCompletedEvaluations(recruitId, request, currentUser);
    }
}
