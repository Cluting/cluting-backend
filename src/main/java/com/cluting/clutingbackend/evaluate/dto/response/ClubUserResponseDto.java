package com.cluting.clutingbackend.evaluate.dto.response;

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
                .clubUserId(entity.getId())
                .userId(entity.getUser().getId())
                .clubId(entity.getClub().getId())
                .role(entity.getRole().toString())
                .permissionLevel(entity.getPermissionLevel())
                .build();
    }
}
