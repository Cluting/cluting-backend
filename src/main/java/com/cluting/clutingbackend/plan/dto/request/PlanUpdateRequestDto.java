package com.cluting.clutingbackend.plan.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanUpdateRequestDto {
    private Long partId;
    private Integer numDoc;
    private Integer numFinal;
}
