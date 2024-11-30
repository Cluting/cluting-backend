package com.cluting.clutingbackend.domain.clubuser.dto;

import com.cluting.clutingbackend.domain.clubuser.ClubUser;
import com.cluting.clutingbackend.global.enums.ClubRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClubUserResponseDto {
    private Long clubUserId;
    private Long userId;
    private Long clubId;
    private ClubRole role;
    private Integer permissionLevel;
    private Integer generation;

    public static ClubUserResponseDto toDto(ClubUser entity) {
        return ClubUserResponseDto.builder()
                .clubUserId(entity.getId())
                .userId(entity.getUser().getId())
                .clubId(entity.getClub().getId())
                .role(entity.getRole())
                .permissionLevel(entity.getPermissionLevel())
                .generation(entity.getGeneration())
                .build();
    }
}
