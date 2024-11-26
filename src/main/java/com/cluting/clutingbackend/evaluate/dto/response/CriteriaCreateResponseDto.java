package com.cluting.clutingbackend.evaluate.dto.response;

import com.cluting.clutingbackend.evaluate.domain.Criteria;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@Builder
public class CriteriaCreateResponseDto {
    private Long id;
    private String partName;
    private String criteria;
    private List<String> detailCriteria;
    private Integer score;
    private Criteria.Stage stage;

    public static CriteriaCreateResponseDto toDto(Criteria entity) {
        return CriteriaCreateResponseDto.builder()
                .id(entity.getCriteriaId())
                .partName(entity.getPart().getName())
                .criteria(entity.getName())
                .detailCriteria(Arrays.asList(entity.getContent().split(":::")))
                .score(entity.getScore())
                .stage(entity.getStage())
                .build();
    }
}
