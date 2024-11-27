package com.cluting.clutingbackend.evaluate.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
// 서류 합격자, 불합격자 조회
public class DocAcceptedOrRejectedResponseDto {
    private Integer totalAccepted;
    private Integer totalRejected;
    private Map<String, Integer> acceptedPart;
    private List<UserPartResponseDto> accepted;
    private List<UserPartResponseDto> rejected;

    public static DocAcceptedOrRejectedResponseDto toDto(Map<String, Integer> acceptedPart, List<UserPartResponseDto> accepted, List<UserPartResponseDto> rejected) {
        return DocAcceptedOrRejectedResponseDto.builder()
                .totalAccepted(accepted.size())
                .totalRejected(rejected.size())
                .acceptedPart(acceptedPart)
                .accepted(accepted)
                .rejected(rejected)
                .build();
    }
}
