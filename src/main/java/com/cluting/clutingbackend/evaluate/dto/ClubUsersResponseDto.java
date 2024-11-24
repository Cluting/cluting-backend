package com.cluting.clutingbackend.evaluate.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ClubUsersResponseDto {
    List<ClubUserResponseDto> clubUsers;

    public static ClubUsersResponseDto toDto(List<ClubUserResponseDto> clubUsers) {
        return ClubUsersResponseDto.builder()
                .clubUsers(clubUsers)
                .build();
    }
}
