package com.cluting.clutingbackend.application.service;

import com.cluting.clutingbackend.application.dto.response.RecruitDetailResponseDto;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitDetailService {

    private final RecruitRepository recruitRepository;

    public RecruitDetailResponseDto getRecruitmentDetails(Long postId) {
        // 모집공고 조회
        Recruit recruit = recruitRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모집공고를 찾을 수 없습니다."));

        // DTO 생성
        return new RecruitDetailResponseDto(
                recruit.getClub().getName(),
                recruit.getImage(),
                recruit.getGeneration(),
                recruit.getNumFinal(),
                recruit.getCreatedAt().toLocalDate(), // 접수 시작일
                recruit.getActivityStart(), // 서류 합격자 발표일
                recruit.getActivityEnd(), // 최종 합격자 발표일
                recruit.getActivityStart().toString() + " ~ " + recruit.getActivityEnd().toString(), // 활동 기간
                recruit.getActivityDay(),
                recruit.getActivityTime(),
                recruit.getClubFee(),
                recruit.getDescription()
        );
    }
}
