package com.cluting.clutingbackend.evaluation.dto.interview;

import com.cluting.clutingbackend.evaluation.dto.document.ApplicantInfo;
import com.cluting.clutingbackend.evaluation.dto.document.EvaluatorScores;
import com.cluting.clutingbackend.evaluation.dto.document.QuestionAndAnswer;
import com.cluting.clutingbackend.global.enums.QuestionType2;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class EachInterviewEvaluationResponse {
    private final ApplicantInfo applicantInfo;
    private final Map<QuestionType2, List<InterviewQA>> groupedQuestions;
    private final List<String> talentProfiles;
    private final Integer averageScore;
    private final List<InterviewEvaluatorScores> evaluatorScores;
    private final InterviewEvaluatorScores myEvaluation;
}
