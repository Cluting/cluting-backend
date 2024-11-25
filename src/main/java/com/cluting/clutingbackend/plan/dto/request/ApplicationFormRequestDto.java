package com.cluting.clutingbackend.plan.dto.request;

import com.cluting.clutingbackend.plan.domain.Option;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ApplicationFormRequestDto {
    private String title; // 지원서 제목
    private Boolean portfolioRequired; // 포트폴리오 제출 여부
    private List<QuestionDto> questions; // 질문 목록

    @Data
    @Builder
    public static class QuestionDto {
        private String content; // 질문 내용
        private String type; // OBJECT or SUBJECT
        private Boolean docOrInterview; // TRUE: 서류, FALSE: 면접
        private String category; // COMMON or PART_SPECIFIC
        private Long partId; // 파트 ID (파트별 질문인 경우)
        private List<Option> choices; // 객관식 선지
        private Boolean multipleChoice; // 복수 선택 여부
    }
}
