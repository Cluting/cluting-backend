package com.cluting.clutingbackend.recruit.dto.request;

import com.cluting.clutingbackend.plan.domain.DocumentCriteria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecruitCriteriaSaveRequestDto {
    private String criteria;
    private List<String> detailCriteria;
    private Integer score;

    public DocumentCriteria toEntity() {
        return DocumentCriteria.builder()
                .name(criteria)
                .content((detailCriteria != null) ? String.join(":::", detailCriteria) : "")
                .score(score)
                .build();
    }
}
