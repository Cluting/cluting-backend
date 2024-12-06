package com.cluting.clutingbackend.plan.service;

import com.cluting.clutingbackend.plan.dto.request.Plan1RequestDto;
import com.cluting.clutingbackend.plan.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final GroupRepository partRepository;

    public void setUpPartNums(List<Plan1RequestDto> plan1RequestDtos){

    }



}
