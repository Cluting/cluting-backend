package com.cluting.clutingbackend.prep.controller;

import com.cluting.clutingbackend.plan.domain.RecruitSchedule;
import com.cluting.clutingbackend.prep.dto.PrepRequestDto;
import com.cluting.clutingbackend.prep.dto.PrepStageDto;
import com.cluting.clutingbackend.prep.dto.RecruitScheduleDto;
import com.cluting.clutingbackend.prep.service.PrepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Tag(name = "계획하기", description = "계획하기 관련 API")
@RestController
@RequestMapping("/api/v1/recruiting/prep")
public class PrepController {
    private final PrepService prepService;

    @Autowired
    public PrepController(PrepService prepService) {
        this.prepService = prepService;
    }


    @Operation(
            summary = "계획 관련 데이터 조회",
            description = "클럽 ID와 모집 공고 ID를 기준으로 리크루팅 일정, 모집 준비 단계, 그룹(파트)를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "계획 데이터 조회 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
            }
    )
    @GetMapping
    public ResponseEntity<?> getRecruitingPrepData(
            @RequestParam Long clubId,
            @RequestParam Long postId) {

        List<RecruitSchedule> recruitSchedules = prepService.getRecruitSchedules(postId);
        List<RecruitScheduleDto> recruitScheduleDtos = recruitSchedules.stream()
                .map(RecruitScheduleDto::new)
                .collect(Collectors.toList());

        List<PrepStageDto> prepStagesWithUserNames = prepService.getPrepStagesWithUserNames(postId);
        List<String> clubUserNames = prepService.getClubUserNames(clubId);
        List<String> groupNames = prepService.getGroupNames(postId);

        Map<String, Object> response = new HashMap<>();
        response.put("recruitSchedules", recruitScheduleDtos);
        response.put("prepStages", prepStagesWithUserNames);
        response.put("clubUserNames", clubUserNames);
        response.put("groupNames", groupNames);

        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "계획 데이터 저장",
            description = "클럽 ID와 모집 공고 ID를 기준으로 계획 데이터를 저장합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "계획 데이터 저장 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다.")
            }
    )
    @PostMapping
    public ResponseEntity<String> createRecruitingPrep(@RequestParam Long clubId,
                                                       @RequestParam Long postId,
                                                       @RequestBody PrepRequestDto request) {
        try {
            prepService.createRecruitingPrep(clubId, postId, request);
            return ResponseEntity.ok("Recruiting preparation data saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error saving recruiting preparation data.");
        }
    }
}
