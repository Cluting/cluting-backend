package com.cluting.clutingbackend.evaluation.dto.response;

import com.cluting.clutingbackend.interview.domain.Interview;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InterviewResultListResponseDto {
    private String name;
    private String phone;
    private String group;

    public static InterviewResultListResponseDto toDto(Interview entity) {
        return InterviewResultListResponseDto.builder()
                .name(entity.getApplication().getUser().getName())
                .phone(entity.getApplication().getUser().getPhone())
                .group(entity.getApplication().getRecruit_group().replaceAll(":::", "/"))
                .build();
    }
}
