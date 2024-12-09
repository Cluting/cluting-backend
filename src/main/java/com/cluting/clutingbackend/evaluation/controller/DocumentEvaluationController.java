package com.cluting.clutingbackend.evaluation.controller;

import com.cluting.clutingbackend.evaluation.dto.DocumentEvaluationRequest;
import com.cluting.clutingbackend.evaluation.dto.DocumentEvaluationResponse;
import com.cluting.clutingbackend.evaluation.service.DocumentEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "[3. 서류 평가하기]", description = "서류 평가 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/eval/doc/{recruitId}")
public class DocumentEvaluationController {

    private final DocumentEvaluationService documentEvaluationService;

    @Operation(
            summary = "[서류 평가하기] 평가 전 상태인 지원서 리스트 불러오기(필터, 정렬 설정 안 한 초기 상태)",
            description = "서류 평가가 완료되지 않은 평가 전 상태인 지원서 리스트를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "서류 평가 전 지원서 리스트 반환 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping
    public List<DocumentEvaluationResponse> getPendingEvaluations(@PathVariable Long recruitId) {
        return documentEvaluationService.getPendingEvaluations(recruitId);
    }

    @Operation(
            summary = "[서류 평가하기] 그룹별 지원서 리스트 불러오기",
            description = "그룹별로 평가된 지원서를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "그룹별 지원서 리스트 반환 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping("/group")
    public List<DocumentEvaluationResponse> getDocumentsByGroup(@PathVariable Long recruitId, @RequestBody DocumentEvaluationRequest request) {
        return documentEvaluationService.getDocumentsByGroup(recruitId, request.getGroupName());
    }

    @Operation(
            summary = "[서류 평가하기] 최신순 지원서 리스트 불러오기",
            description = "최신 지원서를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "최신 지원서 리스트 반환 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping("/newest")
    public List<DocumentEvaluationResponse> getDocumentsByNewest(@PathVariable Long recruitId) {
        return documentEvaluationService.getDocumentsByNewest(recruitId);
    }

    @Operation(
            summary = "[서류 평가하기] 지원서순 리스트 불러오기",
            description = "오래된 지원서를 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "오래된 지원서 리스트 반환 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping("/oldest")
    public List<DocumentEvaluationResponse> getDocumentsByOldest(@PathVariable Long recruitId) {
        return documentEvaluationService.getDocumentsByOldest(recruitId);
    }
}
