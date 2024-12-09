package com.cluting.clutingbackend.plan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plan5RequestDto {

    private String title;                         // 지원서 제목
    private List<PartQuestionDto> partQuestions; // 파트별 질문 목록(여기에 공통은 "공통"으로 들어가있음)
    private Boolean isPortfolioRequired;         // 포트폴리오 여부

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionDto {
        private String content;                  // 질문 내용
        private Boolean isRequired;              // 필수 여부
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PartQuestionDto {
        private String partName;                 // 파트 이름
        private String caution;                  // 해당 파트의 질문 주의사항
        private List<QuestionDto> questions;     // 질문 목록
    }
}
