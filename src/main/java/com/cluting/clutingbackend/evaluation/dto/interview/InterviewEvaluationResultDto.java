package com.cluting.clutingbackend.evaluation.dto.interview;

import com.cluting.clutingbackend.global.enums.EvaluateStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewEvaluationResultDto {
    private Long interviewId;
    private EvaluateStatus state;
    private Integer score;
}
