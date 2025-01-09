package com.cluting.clutingbackend.evaluation.dto.response;

import com.cluting.clutingbackend.clubuser.dto.response.ClubUserResponseDto;
import com.cluting.clutingbackend.user.dto.response.UserResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class LoadDocumentSettingResponseDto {
    Map<String, Participant> group;

    @Data
    @Builder
    public static class Participant {
        private List<ClubUserResponseDto> staff;
        private List<UserResponseDto> applicant;
    }
}
