package com.cluting.clutingbackend.plan.dto.response;

import com.cluting.clutingbackend.plan.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostResponseDto {
    private Long id;
    private Long clubId;
    private String title;
    private String description;
    private String profile;
    private Boolean isDone;
    private String caution;
    private LocalDateTime deadLine;
    private LocalDateTime createdAt;

    public static PostResponseDto toDto(Post entity) {
        return PostResponseDto.builder()
                .id(entity.getId())
                .clubId(entity.getClub().getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .profile(entity.getProfile())
                .isDone(entity.getIsDone())
                .caution(entity.getCaution())
                .deadLine(entity.getDeadLine())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
