package com.cluting.clutingbackend.global.service;

import com.cluting.clutingbackend.global.enums.CurrentStage;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.recruit.dto.response.CurrentStageResponseDto;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentStageService {
    private final RecruitRepository recruitRepository;

    public CurrentStageResponseDto getCurrentStage(Long recruitId) {
        // Recruit 엔티티 조회
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new RuntimeException("Recruit is Not Found"));

        // Recruit의 CurrentStage 가져오기
        CurrentStage currentStage = recruit.getCurrentStage();

        // DTO 생성 및 반환
        return new CurrentStageResponseDto(
                currentStage.name(), // CurrentStage Enum 이름
                currentStage.getDescription(), // 단계 설명
                currentStage.getCurrentState().name() // 현재 상태
        );
    }
}

