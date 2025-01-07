package com.cluting.clutingbackend.evaluation.dto.response;

import com.cluting.clutingbackend.application.domain.Application;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentResultListResponseDto {
    private String name;
    private String phone;
    private String group;
    private String time;

    public static DocumentResultListResponseDto toDto(Application entity, String time) {
        return DocumentResultListResponseDto.builder()
                .name(entity.getUser().getName())
                .phone(entity.getUser().getPhone())
                .group(entity.getRecruit_group().replaceAll(":::", "/"))
                .time(time)
                .build();
    }
}
