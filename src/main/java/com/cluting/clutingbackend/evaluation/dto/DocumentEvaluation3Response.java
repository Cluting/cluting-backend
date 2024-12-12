package com.cluting.clutingbackend.evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentEvaluation3Response {
    private Long applicationId;
    private Integer totalScore;
    private String comment;
    private String status;
}
