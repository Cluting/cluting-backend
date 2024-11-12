package com.cluting.clutingbackend.plan.dto.request;

import com.cluting.clutingbackend.plan.domain.Club;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClubCreateRequestDto {
    private String name;
    private String description;
    private Club.Category category;
    private Club.Type type;
    private List<String> keyword;

    public Club toEntity(String profileUrl) {
        return Club.builder()
                .name(name)
                .description(description)
                .profile(profileUrl)
                .category(category)
                .type(type)
                .keyword((keyword != null) ? String.join(":::", keyword) : "")
                .isRecruiting(false)
                .build();
    }
}
