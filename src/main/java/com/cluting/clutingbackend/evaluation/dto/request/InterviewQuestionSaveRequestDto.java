package com.cluting.clutingbackend.evaluation.dto.request;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewQuestionSaveRequestDto {
    private List<InterviewStaffAllocate> allocates;         // 면접 그룹에 운영진 배정
    private List<String> common;                            // 공통 질문
    private List<InterviewGroupQuestion> group;             // 그룹이 공통일 경우에는 그냥 null 로 받기
    private Map<String, InterviewEvaluateCriteria> criteria; // 면접 평가 기준

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InterviewStaffAllocate {
        private String groupName;
        private List<Long> staff;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InterviewGroupQuestion {
        private String groupName;
        private List<String> question;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InterviewEvaluateCriteria {
        private String name;
        private Integer score;
        private String content;
    }
}
