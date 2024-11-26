package com.cluting.clutingbackend.evaluate.dto.response;

import com.cluting.clutingbackend.plan.domain.Application;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserPartResponseDto {
    private Long userId; // 사용자 id
    private String name; // 사용자 이름
    private String phone; // 사용자 핸드폰번호
    private Application.State state; // 상태
    private String part; // 사용자가 지원한 part
    @Setter
    private Integer ranking; // 순위
    private LocalDateTime createdAt; // 지원한 시간

    public static UserPartResponseDto toDto(Application application) {
        return UserPartResponseDto.builder()
                .userId(application.getUser().getId())
                .name(application.getUser().getName())
                .phone(application.getUser().getPhone())
                .state(application.getState())
                .part(application.getPart())
                .ranking(null)
                .createdAt(application.getCreatedAt())
                .build();
    }
}
