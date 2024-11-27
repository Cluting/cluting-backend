package com.cluting.clutingbackend.docEval.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MyEvaluation {
    private List<ScoreDetail> scoreDetails;
    private String comment;
}