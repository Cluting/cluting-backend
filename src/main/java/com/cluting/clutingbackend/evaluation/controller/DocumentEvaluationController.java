package com.cluting.clutingbackend.evaluation.controller;

import com.cluting.clutingbackend.evaluation.dto.request.MessageSendRequestDto;
import com.cluting.clutingbackend.evaluation.dto.response.DocumentEvaluateResultsResponseDto;
import com.cluting.clutingbackend.evaluation.dto.response.DocumentEvaluationResponse;
import com.cluting.clutingbackend.evaluation.dto.response.DocumentResultListResponseDto;
import com.cluting.clutingbackend.evaluation.service.DocumentEvaluationService;
import com.cluting.clutingbackend.global.enums.EvaluateStatus;
import com.cluting.clutingbackend.global.enums.SortType;
import com.cluting.clutingbackend.evaluation.dto.GroupResponse;
import com.cluting.clutingbackend.evaluation.dto.document.*;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "[3. 서류 평가하기]", description = "서류 평가 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/eval/doc/{recruitId}")
public class DocumentEvaluationController {

    private final DocumentEvaluationService documentEvaluationService;

    @Operation(summary = "[서류 합격자 및 면접 안내] 4-3. 서류 합격자/불합격자 리스트 조회하기",
            description = "입력 받는 state(PASS 또는 FAIL)에 따라 서류 합격자 또는 불합격자 리스트를 조회합니다.")
    @GetMapping("/result/each")
    public List<DocumentResultListResponseDto> getList(
            @PathVariable Long recruitId,
            @RequestParam("state")EvaluateStatus status) {
        return documentEvaluationService.getList(recruitId, status);
    }

    @Operation(summary = "[서류 합격자 및 면접 안내] 4-3. 서류 결과 전송하기",
            description = "입력 받는 전화번호로 입력 받은 메시지를 전송합니다.")
    @PostMapping("/send")
    public ResponseEntity<Void> send(
            @PathVariable Long recruitId,
            @RequestBody MessageSendRequestDto messageSendRequestDto) {
        documentEvaluationService.send(messageSendRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "문자 전송 테스트")
    @PostMapping("/send/test")
    public ResponseEntity<Void> send(
            @PathVariable Long recruitId,
            @RequestParam("phone") String phone) {
        documentEvaluationService.send(phone);
        return ResponseEntity.ok().build();
    }

    // [서류 평가하기] 평가 전 상태 불러오기
    @Operation(summary = "[서류 평가하기] 1. <평가 전> 지원서 리스트 불러오기", description = "서류 평가 상태가 '평가 전'인 지원서들을 불러옵니다. 요청 시 필터링 조건으로 그룹명과 정렬 순서를 지정할 수 있습니다." +
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
    @Operation(summary = "[서류 평가하기] 2. <평가 중> 지원서 리스트 불러오기", description = "서류 평가 상태가 '평가 중' 또는 '편집 가능'인 지원서들을 불러옵니다. 요청 시 필터링 조건으로 그룹명과 정렬 순서를 지정할 수 있습니다." +
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
    @Operation(summary = "[서류 평가하기] 3. <평가 후> 지원서 리스트 불러오기",
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
    @Operation(summary = "[서류 평가하기] 3-1. <평가 후> 완료하기 전송",
            description = "서류 평가 상태가 'READABLE' 또는 'EDITABLE'인 지원서들의 상태를 'AFTER'로 변경합니다.")
    @PostMapping("/after_to_complete")
    public ResponseEntity<Void> updateStagesToAfter(
            @PathVariable Long recruitId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        documentEvaluationService.updateStagesToAfter(recruitId, currentUser);
        return ResponseEntity.ok().build();
    }

    // [서류 평가하기] 평가 완료 불러오기
    @Operation(summary = "[서류 평가하기] 4. <평가 완료> 지원서 리스트 불러오기",
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

    @Operation(summary = "[서류 평가하기] 4-1. <평가 완료> 전송",
            description = "서류 평가가 완료되면 모든 지원서의 상태를 PASS 또는 FAIL로 업데이트하고, 해당 리크루팅 단계도 DOC_PASS로 변경합니다.")
    @PostMapping("/complete-doc")
    public ResponseEntity<String> completeDocumentEvaluation(
            @PathVariable Long recruitId,
            @RequestBody List<DocumentEvaluationCompleteRequest> evaluations) {
        documentEvaluationService.completeDocument2Evaluation(recruitId, evaluations);
        return ResponseEntity.ok("서류 평가 완료되었습니다.");
    }

    @Operation(summary = "[서류 평가하기] 4-2. <평가 완료 수정> 전송",
            description = "서류 평가가 완료되면 모든 지원서의 상태를 PASS 또는 FAIL로 업데이트하고, 해당 리크루팅 단계도 DOC_PASS로 변경합니다.")
    @PatchMapping("/complete-doc")
    public ResponseEntity<String> patchCompleteDocumentEvaluation(
            @PathVariable Long recruitId,
            @RequestBody List<DocumentEvaluationCompleteRequest> evaluations) {
        documentEvaluationService.completeDocument2Evaluation(recruitId, evaluations);
        return ResponseEntity.ok("서류 평가 (PATCH) 완료되었습니다.");
    }

    @Operation(summary = "[서류 평가하기] 4-2. <평가 완료 -이의제기> 지원서 상태를 이의제기 중으로 변경",
            description = "지정된 지원서 ID의 상태를 OBJECTION으로 변경합니다.")
    @PatchMapping("/state/objection")
    public ResponseEntity<String> updateApplicationStateToObjection(
            @PathVariable Long recruitId,
            @RequestParam Long applicationId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        documentEvaluationService.updateApplicationStateToObjection(recruitId, applicationId, currentUser);
        return ResponseEntity.ok("지원서 상태가 OBJECTION으로 변경되었습니다.");
    }

    @Operation(
            summary = "[서류 합격자 및 면접 안내] 4-1. <지원자 합불 경과>",
            description = "서류 합격자, 불합격자 리스트를 반환합니다. sort: NEWEST(최신순) OLDEST(오래된순) INORDER(가나다순)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "서류 합격자, 불합격자 리스트 반환 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping("/result")
    public DocumentEvaluateResultsResponseDto findPassAndFail(
            @PathVariable("recruitId") Long recruitId,
            @RequestParam("sort") SortType sortType) {
        return documentEvaluationService.findDocumentPassAndFail(recruitId, sortType);
    }

    // [서류 평가하기] 지원서 상태를 PASS/FAIL로 변경하고 지원자 정보 반환
    @Operation(summary = "[서류 평가하기] 4-3. <서류 평가 상태 업데이트>",
            description = "지원서 상태를 합격(PASS) 또는 불합격(FAIL)으로 업데이트하고, 해당 지원자 정보(이름, 그룹명, 전화번호, 합격여부)를 반환합니다."+
                            "\n\n합격이면 PASS, 불합격이면 FAIL로 전달해주세요.")
    @PatchMapping("/{applicationId}/evaluate")
    public ResponseEntity<DocumentEvaluation2Response> evaluateApplication(
            @PathVariable Long recruitId,
            @PathVariable Long applicationId,
            @RequestParam String result) {

        // 결과를 PASS 또는 FAIL로 변경 후, 지원자 정보를 포함한 응답 반환
        DocumentEvaluation2Response response = documentEvaluationService.evaluateApplication(recruitId, applicationId, result);
        return ResponseEntity.ok(response);
    }

    // ---

    // 서류 평가
    @Operation(summary = "[서류 평가 전송]", description = "지원서에 대한 기준별 점수와 코멘트를 저장합니다.")
    @PostMapping("/{applicationId}/doc-evalute")
    public ResponseEntity<DocumentEvaluation3Response> evaluateDocument(
            @PathVariable Long recruitId,
            @PathVariable Long applicationId,
            @Valid @RequestBody DocumentEvaluation3Request request) {

        DocumentEvaluation3Response response = documentEvaluationService.evaluateDocument(recruitId, applicationId, request);
        return ResponseEntity.ok(response);
    }

    // 서류평가
    @Operation(summary = "[서류 평가 내용 가져오기]", description = "지원서 정보 및 내용, 기준별 점수와 코멘트를 가져옵니다.")
    @GetMapping("/{applicationId}/doc-evaluate")
    public ResponseEntity<DocumentEvaluation4Response> getDocumentEvaluation(
            @PathVariable Long recruitId,
            @PathVariable Long applicationId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        DocumentEvaluation4Response response = documentEvaluationService.getDocumentEvaluation(recruitId, applicationId, currentUser);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "[서류 평가하기-필터링 용] 그룹명 가져오기")
    @GetMapping("/groups")
    public ResponseEntity<List<GroupResponse>> getGroupsByRecruitId(@RequestParam Long recruitId) {
        List<GroupResponse> groups = documentEvaluationService.getGroupsByRecruitId(recruitId);
        return ResponseEntity.ok(groups);
    }
}
