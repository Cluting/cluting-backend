package com.cluting.clutingbackend.evaluation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
    private EvaluatorInfo currentEvaluator;          // 현재 로그인한 유저의 정보
    private List<EvaluatorInfo> otherEvaluators;     // 다른 운영진 정보

    @Data
    @AllArgsConstructor
    public static class EvaluatorInfo {
        private String name;   // 운영진 이름
        private String state;  // 평가 상태
    }
}

