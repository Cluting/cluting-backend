package com.cluting.clutingbackend.evaluate.dto;

import com.cluting.clutingbackend.plan.domain.ClubUser;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClubUserResponseDto {
    private Long clubUserId;
    private Long userId;
    private Long clubId;
    private String role;
    private Integer permissionLevel;

    public static ClubUserResponseDto toDto(ClubUser entity) {
        return ClubUserResponseDto.builder()
                .clubUserId(entity.getClubUserId())
                .userId(entity.getUser().getId())
                .clubId(entity.getClub().getId())
                .role(entity.getRole().toString())
                .permissionLevel(entity.getPermissionLevel())
                .build();
    }
}
