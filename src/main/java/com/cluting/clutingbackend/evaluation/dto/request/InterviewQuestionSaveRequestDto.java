package com.cluting.clutingbackend.evaluation.dto.request;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewQuestionSaveRequestDto {
    private List<InterviewStaffAllocate> allocates;         // 면접 그룹에 운영진 배정
    private List<String> common;                            // 공통 질문
    private List<InterviewGroupQuestion> group;             // 그룹이 공통일 경우에는 그냥 null 로 받기
    private List<InterviewIndividualQuestion> individual;   // 개인 질문

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
    public static class InterviewIndividualQuestion {
        private String name;
        private String phone; // 이름과 번호로 특정
        private List<String> question;
    }
}
