package com.cluting.clutingbackend.recruit.controller;


import com.cluting.clutingbackend.global.enums.Category;
import com.cluting.clutingbackend.global.enums.ClubType;
import com.cluting.clutingbackend.global.enums.SortType;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.recruit.dto.request.RecruitDocSetRequestDto;
import com.cluting.clutingbackend.recruit.dto.response.RecruitDocPrepSavedResponseDto;
import com.cluting.clutingbackend.recruit.dto.response.RecruitNumResponseDto;
import com.cluting.clutingbackend.recruit.dto.response.RecruitResponseDto;
import com.cluting.clutingbackend.recruit.dto.response.RecruitsResponseDto;
import com.cluting.clutingbackend.recruit.service.RecruitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[리크루팅 홈]", description = "리크루팅 홈 관련 API")
@RestController
@RequestMapping("/api/v1/recruit")
@RequiredArgsConstructor
public class RecruitController {
    private final RecruitService recruitService;


    @Operation(
            summary = "홈화면 동아리 리스트 조회",
            description = "홈화면 동아리 리스트를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "홈화면 동아리 리스트 조회되었습니다."),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
            }
    )
    @GetMapping("/list")
    @ResponseStatus(value = HttpStatus.OK)
    public RecruitsResponseDto findPosts(
            @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
            @RequestParam(value = "sortType", required = false) SortType sortType, // 마감임박순, 최신순, 오래된 순
            @RequestParam(value = "clubType", required = false) ClubType clubType, // 연합동아리, 교내동아리
            @RequestParam(value = "fieldType", required = false) Category category) { // 동아리 분류
        return recruitService.findAll(pageNum, sortType, clubType, category);
    }

    @Operation(
            summary = "ID로 리크루팅 단일 조회",
            description = "ID로 리크루팅 단일 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "ID로 리크루팅 단일 조회되었습니다."),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
            }
    )
    @GetMapping("/{recruitId}")
    @ResponseStatus(value = HttpStatus.OK)
    public RecruitResponseDto findPosts(
            @PathVariable("recruitId") Long recruitId) {
        return recruitService.findById(recruitId);
    }

    @Operation(
            summary = "모집 공고에 지원한 지원자 수 조회",
            description = "모집 공고에 지원한 지원자 수 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "모집 공고에 지원한 지원자 수 조회되었습니다."),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
            }
    )
    @GetMapping("/apply/{recruitId}")
    @ResponseStatus(value = HttpStatus.OK)
    public RecruitNumResponseDto findAppliedNum(
            @PathVariable("recruitId") Long recruitId) {
        return recruitService.findAppliedNum(recruitId);
    }

    @Operation(
            summary = "이전 과정에서 설정한 서류 합격자 수 목록 조회",
            description = "이전 과정에서 설정한 서류 합격자 수 목록 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "이전 과정에서 설정한 서류 합격자 수 목록 조회되었습니다."),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
            }
    )
    @GetMapping("/doc/pre/{recruitId}")
    @ResponseStatus(value = HttpStatus.OK)
    public RecruitNumResponseDto findDocPassNum(
            @PathVariable("recruitId") Long recruitId) {
        return recruitService.findDocPassNum(recruitId);
    }

    @Operation(
            summary = "서류 평가 준비하기 설정 저장",
            description = "서류 평가 준비하기 설정을 저장합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "서류 평가 준비하기 설정이 저장되었습니다."),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
            }
    )
    @PostMapping("/doc/pre/{recruitId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public RecruitDocPrepSavedResponseDto saveDocRecruit(
            @PathVariable("recruitId") Long recruitId,
            @RequestBody RecruitDocSetRequestDto recruitDocSetRequestDto) {
        return recruitService.saveDocRecruit(recruitId, recruitDocSetRequestDto);

    }
}
