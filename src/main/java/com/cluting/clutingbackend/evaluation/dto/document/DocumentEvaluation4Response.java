package com.cluting.clutingbackend.evaluation.dto.document;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DocumentEvaluation4Response {
    private ApplicantInfo applicantInfo;                  // 지원자 정보
    private List<QuestionAndAnswer> questionAndAnswers;   // 질문 및 답변 리스트
    private  List<String> talentProfiles;                 // 인재상 (프로필)
    private Integer averageScore;                         // 평균 점수
    private List<EvaluatorScores> evaluatorScores;        // 다른 평가자의 점수
    private EvaluatorScores myEvaluation;                 // 자신의 평가 정보
}
