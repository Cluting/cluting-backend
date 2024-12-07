package com.cluting.clutingbackend.club.dto.response;

import com.cluting.clutingbackend.club.domain.Club;
import com.cluting.clutingbackend.global.enums.Category;
import com.cluting.clutingbackend.global.enums.ClubType;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
public class ClubResponseDto {
    private Long id;
    private List<ClubRecruitResponseDto> recruits;
    private String name;
    private String description;
    private String profile;
    private Category category;
    private ClubType type;
    private List<String> keyword;
    private Boolean isRecruiting;

    public static ClubResponseDto toDto(Club entity) {
        return ClubResponseDto.builder()
                .id(entity.getId())
                .recruits(entity.getRecruits() != null ? entity.getRecruits().stream().map(ClubRecruitResponseDto::toDto).toList() : Collections.emptyList())
                .name(entity.getName())
                .description(entity.getDescription())
                .profile(entity.getProfile())
                .category(entity.getCategory())
                .type(entity.getType())
                .keyword(Arrays.stream(entity.getKeyword().split(":::")).toList())
                .isRecruiting(entity.getIsRecruiting())
                .build();
    }
}
