package com.cluting.clutingbackend.evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// [서류 평가하기] 지원서 리스트
@Getter
@Setter
@AllArgsConstructor
public class DocumentEvaluationResponse {
    private String evaluationStage;
    private String applicantName;
    private String applicantPhone;
    private String groupName;
    private String applicationNumClubUser;
    private LocalDateTime createdAt;

}
