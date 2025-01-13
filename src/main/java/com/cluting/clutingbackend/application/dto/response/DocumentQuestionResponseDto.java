package com.cluting.clutingbackend.application.dto.response;

import com.cluting.clutingbackend.global.enums.QuestionType;
import com.cluting.clutingbackend.plan.domain.DocumentQuestion;
import com.cluting.clutingbackend.plan.domain.Option;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DocumentQuestionResponseDto {
    private Long id;
    private List<String> options;
    private QuestionType questionType;
    private String content;
    private Integer wordLimit;

    public static DocumentQuestionResponseDto toDto(DocumentQuestion entity) {
        return DocumentQuestionResponseDto.builder()
                .id(entity.getId())
                .options(entity.getOptionList().stream().map(Option::getContent).toList())
                .questionType(entity.getQuestionType())
                .content(entity.getContent())
                .wordLimit(entity.getWordLimit())
                .build();
    }
}
