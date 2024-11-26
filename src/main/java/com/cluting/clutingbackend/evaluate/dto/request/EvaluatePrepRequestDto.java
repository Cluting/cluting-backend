package com.cluting.clutingbackend.evaluate.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EvaluatePrepRequestDto {
    private List<GroupStaffAllocateRequestDto> groups;
    private List<EvaluateCriteriaRequestDto> criteria;
    private Integer maxScore; // TODO 평가 테이블에 저장해야 함
}
