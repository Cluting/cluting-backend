package com.cluting.clutingbackend.plan.controller;

import com.cluting.clutingbackend.plan.dto.request.InterviewSetupDto;
import com.cluting.clutingbackend.plan.service.InterviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//@Tag(name="모집하기(4) 면접 일정 조정",description = "when2meet 기능 구현")
//@RestController
//public class InterviewController {
//    @Autowired
//    private InterviewService interviewService;
//
//    @Operation(summary = "면접 기본 세팅 API",description = "면접관과 면접자 인원,면접 소요 시간,면접 기간 및 시간대 설정")
//    @PostMapping("/setup")
//    public ResponseEntity<String> setupInterview(@RequestBody InterviewSetupDto setUpDto){
//        return ResponseEntity.ok("Interview setup completed.");
//    }
//
//    @Operation(summary = "운영진 면접 가능 시간 선택")
//    @PostMapping("/slots/update/{clubUserId}")
//    public ResponseEntity<String> updateSlots(@PathVariable Long clubUserId, @RequestBody Map<String, Boolean> slots) {
//        interviewService.updateSlots(clubUserId, slots);
//        return ResponseEntity.ok("Slots updated for club user ID: " + clubUserId);
//    }
//
//    @Operation(summary = "면접 일정 확정")
//    @GetMapping("/confirm-schedule")
//    public ResponseEntity<List<InterviewAssignment>> confirmInterviewSchedule(@RequestParam List<Long> clubUserIds) {
//        List<InterviewAssignment> assignments = interviewService.confirmInterviewSchedule(clubUserIds);
//        return ResponseEntity.ok(assignments);
//    }
//
//}
