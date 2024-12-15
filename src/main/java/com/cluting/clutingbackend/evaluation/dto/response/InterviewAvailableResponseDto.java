package com.cluting.clutingbackend.evaluation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class InterviewAvailableResponseDto {
    private LocalDateTime time;
    private List<UserResponseDto> staff;
    private List<UserResponseDto> applicant;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResponseDto {
        private String id;
        private String name;
    }
}
