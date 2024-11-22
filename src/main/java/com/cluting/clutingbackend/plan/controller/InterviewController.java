package com.cluting.clutingbackend.plan.controller;

import com.cluting.clutingbackend.plan.domain.Interview;
import com.cluting.clutingbackend.plan.dto.request.InterviewSetupDto;
import com.cluting.clutingbackend.plan.dto.request.TimeSlotAvailabilityDto;
import com.cluting.clutingbackend.plan.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interview")
@RequiredArgsConstructor
public class InterviewController {
    private final InterviewService interviewService;

    // 면접 기본 설정 등록
    @PostMapping("/setup")
    public ResponseEntity<Interview> setupInterview(@RequestBody InterviewSetupDto dto) {
        return ResponseEntity.ok(interviewService.createInterview(dto));
    }

    // 운영진 면접 가능 시간 저장
    @PostMapping("/{interviewId}/availability")
    public ResponseEntity<Void> saveAvailability(
            @PathVariable Long interviewId,
            @RequestBody TimeSlotAvailabilityDto dto) {
        interviewService.saveAvailability(interviewId, dto);
        return ResponseEntity.ok().build();
    }

    // 시간대별 가능한 운영진 조회
    @GetMapping("/{interviewId}/available")
    public ResponseEntity<Map<LocalDateTime, List<Long>>> getAvailableInterviewers(@PathVariable Long interviewId) {
        return ResponseEntity.ok(interviewService.getAvailableInterviewers(interviewId));
    }
}
