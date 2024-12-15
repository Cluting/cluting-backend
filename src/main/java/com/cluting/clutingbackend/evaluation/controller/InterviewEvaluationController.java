package com.cluting.clutingbackend.evaluation.controller;

import com.cluting.clutingbackend.evaluation.service.InterviewEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/eval/inv")
public class InterviewEvaluationController {
    private final InterviewEvaluationService interviewEvaluationService;

    // 면접 가능 일정 리스트 조회

    // 면접 일정 저장
}
