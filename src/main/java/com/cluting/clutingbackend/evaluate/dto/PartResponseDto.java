package com.cluting.clutingbackend.evaluate.dto;

import com.cluting.clutingbackend.plan.domain.Part;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PartResponseDto {
    private String name; // 파트 이름
    private Integer numDoc; // 해당 파트에 합격할 지원자 서류 수
    private Integer numFinal; // 해당 파트에 최종 합격할 수
    private Integer numRecruit; // 해당 파트에 지원한 지원서 수

    public static PartResponseDto toDto(Part entity) {
        return PartResponseDto.builder()
                .name(entity.getName())
                .numDoc(entity.getNumDoc())
                .numFinal(entity.getNumFinal())
                .numRecruit(entity.getNumRecruit())
                .build();
    }
}
