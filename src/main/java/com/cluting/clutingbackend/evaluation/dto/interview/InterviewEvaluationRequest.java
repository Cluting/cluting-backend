package com.cluting.clutingbackend.evaluation.dto.interview;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewEvaluationRequest {
    private String groupName;  // 그룹명 필터
    private String sortOrder; // 정렬 기준 (newest, oldest)
}