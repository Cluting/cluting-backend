package com.cluting.clutingbackend.recruit.dto.response;

import com.cluting.clutingbackend.global.enums.Category;
import com.cluting.clutingbackend.global.enums.ClubType;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RecruitResponseDto {
    private Long id;
    private Long clubId;
    private String title;
    private String description;
    private Category category;
    private ClubType clubType;
    private String profile;
    private Boolean isDone;
    private String caution;
    private LocalDateTime deadLine;
    private LocalDateTime createdAt;

    public static RecruitResponseDto toDto(Recruit entity) {
        return RecruitResponseDto.builder()
                .id(entity.getId())
                .clubId(entity.getClub().getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .clubType(entity.getClub().getType())
                .category(entity.getClub().getCategory())
                .profile(entity.getImage())
                .isDone(entity.getIsDone())
                .caution(entity.getCaution())
                .deadLine(entity.getDeadLine())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
