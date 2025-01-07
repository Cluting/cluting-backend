package com.cluting.clutingbackend.evaluation.dto.interview;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.global.enums.Stage;
import com.cluting.clutingbackend.interview.domain.Interview;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class InterviewEvaluationResultResponseDto {
    // 합격자
    private Integer passedCnt;
    private Map<String, Integer> byGroup;
    private List<InterviewEvaluateResult> passed;

    // 불합격자
    private Integer failedCnt;
    private List<InterviewEvaluateResult> failed;

    @Data
    @Builder
    public static class InterviewEvaluateResult {
        private Stage state;
        private String name;
        private String phone;
        private String part;
        private Integer score;
        private Integer rank;
        private LocalDateTime createdAt;
        private String result;

        public static InterviewEvaluateResult toDto(Interview entity, Stage stage, String result) {
            return InterviewEvaluateResult.builder()
                    .state(stage)
                    .name(entity.getApplication().getUser().getName())
                    .phone(entity.getApplication().getUser().getPhone())
                    .part(entity.getRecruit_group().replaceAll(":::", "/"))
                    .score(entity.getScore())
                    .rank(null)
                    .createdAt(entity.getApplication().getCreatedAt())
                    .result(result)
                    .build();
        }
    }
}
