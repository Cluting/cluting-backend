package com.cluting.clutingbackend.recruit.service;

import com.cluting.clutingbackend.global.enums.Category;
import com.cluting.clutingbackend.global.enums.ClubType;
import com.cluting.clutingbackend.global.enums.SortType;
import com.cluting.clutingbackend.global.util.StaticValue;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.recruit.dto.response.RecruitResponseDto;
import com.cluting.clutingbackend.recruit.dto.response.RecruitsResponseDto;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import com.cluting.clutingbackend.recruit.repository.RecruitScheduleRepository;
import com.cluting.clutingbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitService {
    private final RecruitRepository recruitRepository;
    private final RecruitScheduleRepository recruitScheduleRepository;

    @Transactional(readOnly = true)
    public RecruitsResponseDto findAll(Integer pageNum, SortType sortType, ClubType clubType, Category category) {
        int pageSize = StaticValue.PAGE_DEFAULT_SIZE;
        int skipCount = (pageNum - 1) * pageSize;

        List<RecruitResponseDto> recruitDtos = recruitRepository.findAll().stream()
                .map(RecruitResponseDto::toDto)
                .filter(dto -> clubType == null || dto.getClubType() == clubType)
                .filter(dto -> category == null || dto.getCategory() == category)
                .sorted((dto1, dto2) -> {
                    if (sortType == null) return 0;
                    return switch (sortType) {
                        case DEADLINE -> dto1.getDeadLine().compareTo(dto2.getDeadLine());
                        case NEWEST -> dto2.getCreatedAt().compareTo(dto1.getCreatedAt());
                        case OLDEST -> dto1.getCreatedAt().compareTo(dto2.getCreatedAt());
                        default -> 0;
                    };
                })
                .skip(skipCount)
                .limit(pageSize)
                .toList();

        return new RecruitsResponseDto(recruitDtos.size(), recruitDtos);
    }

    @Transactional(readOnly = true)
    public RecruitResponseDto findById(Long recruitId) {
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "존재하지 않는 리크루팅 입니다."
                        )
                );

        return RecruitResponseDto.toDto(recruit);
    }
}
