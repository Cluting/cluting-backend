package com.cluting.clutingbackend.evaluation.controller;

import com.cluting.clutingbackend.evaluation.dto.response.InterviewPrepResponseDto;
import com.cluting.clutingbackend.evaluation.service.InterviewEvaluationService;
import com.cluting.clutingbackend.recruit.dto.response.RecruitNumResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/eval/inv")
public class InterviewEvaluationController {
    private final InterviewEvaluationService interviewEvaluationService;

    // 면접 가능 일정 리스트 조회

    // 면접 일정 저장

    @Operation(
            summary = "[면접 평가하기] 모집 그룹 공통/다수 여부 확인",
            description = "모집 그룹 공통/다수 여부를 확인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "모집 그룹 공통/다수 여부 확인 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping("/check/{recruitId}")
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
    @GetMapping("/num/{recruitId}")
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
    @GetMapping("/prep/{recruitId}")
    public List<InterviewPrepResponseDto> findApplicants(
            @PathVariable("recruitId") Long recruitId) {
        return interviewEvaluationService.findApplicants(recruitId);
    }
}
