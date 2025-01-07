package com.cluting.clutingbackend.evaluation.dto.response;

import com.cluting.clutingbackend.application.domain.Application;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InterviewPrepResponseDto {
    private String name;
    private String phone;
    private String groupName;

    public static InterviewPrepResponseDto toDto(Application entity) {
        return InterviewPrepResponseDto.builder()
                .name(entity.getUser().getName())
                .phone(entity.getUser().getPhone())
                .groupName(entity.getRecruit_group().replaceAll(":::", "/"))
                .build();
    }
}
