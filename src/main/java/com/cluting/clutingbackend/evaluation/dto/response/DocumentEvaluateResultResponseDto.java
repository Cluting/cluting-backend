package com.cluting.clutingbackend.evaluation.dto.response;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.global.enums.Stage;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DocumentEvaluateResultResponseDto {
    private Stage state;
    private String name;
    private String phone;
    private String part;
    private Integer score;
    private Integer rank;
    private LocalDateTime createdAt;
    private String result;

    public static DocumentEvaluateResultResponseDto toDto(Application entity, Stage stage, String result) {
        return DocumentEvaluateResultResponseDto.builder()
                .state(stage)
                .name(entity.getUser().getName())
                .phone(entity.getUser().getPhone())
                .part(entity.getRecruit_group().replaceAll(":::", "/"))
                .score(entity.getScore())
                .rank(null)
                .createdAt(entity.getCreatedAt())
                .result(result)
                .build();
    }
}
