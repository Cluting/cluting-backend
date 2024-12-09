package com.cluting.clutingbackend.plan.dto.response;

import com.cluting.clutingbackend.plan.dto.request.Plan5RequestDto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plan5ResponseDto {

    private String title;                         // 지원서 제목
    private List<Plan5RequestDto.PartQuestionDto> partQuestions; // 파트별 질문 목록
    private Boolean isPortfolioRequired;         // 포트폴리오 여부

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionDto {
        private String content;                  // 질문 내용
        private Boolean isRequired;              // 필수 여부
    }

}

