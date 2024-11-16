package com.cluting.clutingbackend.plan.dto.response;

import com.cluting.clutingbackend.plan.domain.Club;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ClubResponseDto {
    private Long id;
    private String name;
    private String description;
    private String profile;
    private Club.Category category;
    private Club.Type type;
    private List<String> keyword;
    private Boolean isRecruiting;

    public static ClubResponseDto toDto(Club entity) {
        return ClubResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .profile(entity.getProfile())
                .category(entity.getCategory())
                .type(entity.getType())
                .keyword(List.of(entity.getKeyword().split(":::")))
                .isRecruiting(entity.getIsRecruiting())
                .build();
    }
}
