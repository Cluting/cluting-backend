package com.cluting.clutingbackend.plan.service;

import com.cluting.clutingbackend.plan.dto.request.Plan1RequestDto;
import com.cluting.clutingbackend.plan.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {
    //공갈빵, 피스타치오크럼핀, 샐러드+소세지, 그릭요거트, 땅콩버터
    @Autowired
    private GroupRepository groupRepository;

    public void setUpPartNums(List<Plan1RequestDto> plan1RequestDtos){

    }



}
