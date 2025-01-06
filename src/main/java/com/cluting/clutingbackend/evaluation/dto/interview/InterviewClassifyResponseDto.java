package com.cluting.clutingbackend.evaluation.dto.interview;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class InterviewClassifyResponseDto {
    private Map<LocalDateTime, InterviewClassify> list;

    @Data
    public static class InterviewClassify {
        private List<InterviewAssign> staff;
        private List<InterviewAssign> applicant;
    }

    @Data
    public static class InterviewAssign {
        private Long id; // 운영진일 경우엔 clubUserId, 지원자일 경우엔 userId
        private String name;
    }
}
