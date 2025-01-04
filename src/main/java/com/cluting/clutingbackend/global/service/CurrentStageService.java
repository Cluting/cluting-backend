package com.cluting.clutingbackend.global.service;

import com.cluting.clutingbackend.global.enums.CurrentStage;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentStageService {
    private final RecruitRepository recruitRepository;

    public CurrentStage getCurrentStage(Long recruitId){
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(()-> new RuntimeException("Recruit is Not Found"));
        return recruit.getCurrentStage();
    }
}
