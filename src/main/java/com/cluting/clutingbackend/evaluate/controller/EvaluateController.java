package com.cluting.clutingbackend.evaluate.controller;

import com.cluting.clutingbackend.evaluate.dto.request.EvaluatePrepRequestDto;
import com.cluting.clutingbackend.evaluate.dto.response.ClubUsersResponseDto;
import com.cluting.clutingbackend.evaluate.dto.response.DocAcceptedOrRejectedResponseDto;
import com.cluting.clutingbackend.evaluate.dto.response.DocEvaluatePrepResponseDto;
import com.cluting.clutingbackend.evaluate.dto.response.DocPrepareResponseDto;
import com.cluting.clutingbackend.evaluate.service.EvaluateService;
import com.cluting.clutingbackend.util.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/v1/evaluate")
@RequiredArgsConstructor
public class EvaluateController {
    private final EvaluateService evaluateService;

    @Operation(description = "서류 평가 준비 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "서류 준비 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "서류 준비 정보 조회 실패"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/prepare/{postId}")
    public DocEvaluatePrepResponseDto docEvaluatePrep(@PathVariable("postId") Long postId) {
        return evaluateService.docEvaluatePrep(postId);
    }

    @Operation(description = "서류 평가 위한 임원진 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "임원진 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "임원진 목록 조회 실패"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/staffs/{postId}")
    public ClubUsersResponseDto clubStaffList(@PathVariable("postId") Long postId) {
        return evaluateService.clubStaffList(postId);
    }

    @Operation(description = "서류 평가 준비하기 저장")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "서류 평가 준비하기 저장 성공"),
            @ApiResponse(responseCode = "404", description = "서류 평가 준비하기 저장 실패"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PostMapping("/prep/{postId}")
    public DocPrepareResponseDto prepareDocEvaluate(
            @PathVariable("postId") Long postId,
            @RequestBody EvaluatePrepRequestDto evaluatePrepRequestDto) {
        return evaluateService.prepareDocEvaluate(postId, evaluatePrepRequestDto);
    }

    @Operation(description = "서류 평가 결과 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "서류 평가 결과 조회 성공"),
            @ApiResponse(responseCode = "404", description = "서류 평가 결과 조회 실패"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/result/{applicationId}")
    public DocAcceptedOrRejectedResponseDto acceptedOrRejected(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("applicationId") Long applicationId,
            @RequestParam("recent") Boolean recent,
            @RequestParam("part") String part) {
        return evaluateService.acceptedOrRejected(userDetails.getUser(), applicationId, recent, part);
    }

    @Operation(description = "모집 공고의 파트명 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모집 공고의 파트명 목록 성공"),
            @ApiResponse(responseCode = "404", description = "모집 공고의 파트명 목록 실패"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/part/{applicationId}")
    public List<String> partList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("applicationId") Long applicationId) {
        return evaluateService.partList(applicationId);
    }
}
