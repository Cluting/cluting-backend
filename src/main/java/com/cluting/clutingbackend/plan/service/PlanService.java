package com.cluting.clutingbackend.plan.service;

import com.cluting.clutingbackend.plan.dto.request.Plan1RequestDto;
import com.cluting.clutingbackend.plan.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {

    @Autowired
    private PartRepository partRepository;

    public void setUpPartNums(List<Plan1RequestDto> plan1RequestDtos){

    }



}
