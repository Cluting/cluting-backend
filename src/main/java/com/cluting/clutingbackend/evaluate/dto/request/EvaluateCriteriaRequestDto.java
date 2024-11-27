package com.cluting.clutingbackend.evaluate.dto.request;

import com.cluting.clutingbackend.evaluate.domain.Criteria;
import com.cluting.clutingbackend.part.Part;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EvaluateCriteriaRequestDto {
    private String groupName;
    private String criteria;
    private List<String> detailCriteria;
    private Integer score;

    public Criteria toEntity(Part part) {
        return Criteria.builder()
                .part(part)
                .interview(null)
                .name(criteria)
                .content(String.join(":::", detailCriteria))
                .score(score)
                .stage(Criteria.Stage.DOCUMENT)
                .build();
    }
}
