package com.cluting.clutingbackend.plan.controller;

import com.cluting.clutingbackend.plan.domain.Interview;
import com.cluting.clutingbackend.plan.dto.request.InterviewAssignmentDto;
import com.cluting.clutingbackend.plan.dto.request.InterviewSetupDto;
import com.cluting.clutingbackend.plan.dto.request.TimeSlotAvailabilityDto;
import com.cluting.clutingbackend.plan.service.InterviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interview")
@RequiredArgsConstructor
@Tag(name = "모집하기(4) - 면접 일정 조정하기")
public class InterviewController {
    private final InterviewService interviewService;

    // 면접 기본 설정 등록
    @Operation(summary = "면접 기본 설정 등록", description = "면접관 수, 면접자 수, 면접 시간을 설정합니다.")
    @PostMapping("/setup")
    public ResponseEntity<Interview> setupInterview(@RequestBody InterviewSetupDto dto) {
        return ResponseEntity.ok(interviewService.createInterview(dto));
    }

    // 운영진 면접 가능 시간 저장
    @Operation(summary = "운영진 면접 가능 시간 저장", description = "운영진이 선택한 면접 가능 시간대를 저장합니다.")
    @PostMapping("/{interviewId}/availability")
    public ResponseEntity<Void> saveAvailability(
            @PathVariable Long interviewId,
            @RequestBody TimeSlotAvailabilityDto dto) {
        interviewService.saveAvailability(interviewId, dto);
        return ResponseEntity.ok().build();
    }

    // 시간대별 가능한 운영진 조회
    @Operation(summary = "시간대별 가능한 운영진 조회", description = "저장된 데이터를 기반으로, 시간대별 가능한 운영진 리스트를 반환합니다.")
    @GetMapping("/{interviewId}/available")
    public ResponseEntity<Map<LocalDateTime, Map<String, Object>>> getAvailableInterviewers(@PathVariable Long interviewId) {
        return ResponseEntity.ok(interviewService.getAvailableInterviewers(interviewId));
    }

    @Operation(summary = "면접관 일정 확정하기")
    @PostMapping("/{interviewId}/assign")
    public ResponseEntity<Void> assignInterviewers(
            @PathVariable Long interviewId,
            @RequestBody InterviewAssignmentDto dto) {
        interviewService.assignInterviewers(interviewId, dto);
        return ResponseEntity.ok().build();
    }
}
