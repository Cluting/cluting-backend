package com.cluting.clutingbackend.evaluation.dto.interview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class InterviewEvaluationResponse {
    private String stage; // BEFORE, ING, AFTER
    private String applicantName;
    private String applicantPhone;
    private String groupName;
    private String evaluationStatus; // <현재 평가한 운영진 수>/<평가할 전체 운영진 수>
}