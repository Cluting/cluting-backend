package com.cluting.clutingbackend.interview.controller;

import com.cluting.clutingbackend.interview.dto.InterviewAnswerRequestDto;
import com.cluting.clutingbackend.interview.dto.InterviewDetailsDto;
import com.cluting.clutingbackend.interview.service.InterviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[5. 면접 답변 기록]", description = "면접 답변 기록 관련 API")
@RestController
@RequestMapping("/api/v1/interview")
@RequiredArgsConstructor
public class InterviewController {
    private final InterviewService interviewService;

    @Operation(summary = "면접 답변 기록하기")
    @GetMapping("/{interviewId}")
    public ResponseEntity<InterviewDetailsDto> getInterviewDetails(@PathVariable Long interviewId) {
        InterviewDetailsDto details = interviewService.getInterviewDetails(interviewId);
        return ResponseEntity.ok(details);
    }

    @PostMapping
    public ResponseEntity<Void> saveInterviewAnswers(@RequestBody @Valid InterviewAnswerRequestDto requestDto) {
        interviewService.saveInterviewAnswers(requestDto);
        return ResponseEntity.ok().build();
    }
}