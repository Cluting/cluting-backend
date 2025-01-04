package com.cluting.clutingbackend.clubuser.dto.response;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.global.enums.ClubRole;
import com.cluting.clutingbackend.global.enums.PermissionLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ClubUserResponseDto {
    private Long id;
    private Long userId;
    private Long clubId;
    private String name;
    private String email;
    private ClubRole role;
    private List<PermissionLevel> permissionLevel;
    private Integer generation;

    public static ClubUserResponseDto toDto(ClubUser entity) {
        return ClubUserResponseDto.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .clubId(entity.getClub().getId())
                .name(entity.getUser().getName())
                .email(entity.getUser().getEmail())
                .role(entity.getRole())
                .permissionLevel(entity.getPermissionLevels())
                .generation(entity.getGeneration())
                .build();
    }
}
