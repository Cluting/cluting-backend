package com.cluting.clutingbackend.docEval.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class EvaluationResponse {
    private ApplicantInfo applicantInfo;
    private List<QuestionContent> questionContents;
    private MyEvaluation myEvaluations;
    private List<Map<String, Object>> talentProfiles;
    private List<AdminEvaluation> adminEvaluations;
}