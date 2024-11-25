package com.cluting.clutingbackend.evaluate.dto.request;

import com.cluting.clutingbackend.evaluate.domain.Evaluator;
import com.cluting.clutingbackend.part.Part;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GroupStaffAllocateRequestDto {
    private String groupName;
    private List<Long> staffs;

    public Evaluator toEntity(Part part) {
        return Evaluator.builder()
                .partName(groupName)
                .part(part)
                .evaluators(new ArrayList<>())
                .build();
    }
}
