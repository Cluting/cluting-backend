package com.cluting.clutingbackend.evaluate.dto.response;

import com.cluting.clutingbackend.evaluate.domain.Criteria;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CriteriaCreateResponseDto {
    private Long id;
    private String partName;
    private String criteria;
    private String detailCriteria;
    private Integer score;
    private Criteria.Stage stage;

    public static CriteriaCreateResponseDto toDto(Criteria entity) {
        return CriteriaCreateResponseDto.builder()
                .id(entity.getCriteriaId())
                .partName(entity.getPart().getName())
                .criteria(entity.getName())
                .detailCriteria(entity.getContent())
                .score(entity.getScore())
                .stage(entity.getStage())
                .build();
    }
}
