package com.cluting.clutingbackend.club.dto.request;

import com.cluting.clutingbackend.club.domain.Club;
import com.cluting.clutingbackend.global.enums.Category;
import com.cluting.clutingbackend.global.enums.ClubType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClubRegisterRequestDto {
    private String name;
    private String description;
    private Category category;
    private ClubType type;
    private List<String> keyword;

    public Club toEntity(String imageUrl) {
        return Club.builder()
                .name(name)
                .description(description)
                .profile(imageUrl)
                .category(category)
                .type(type)
                .keyword((keyword != null) ? String.join(":::", keyword) : "")
                .isRecruiting(false)
                .build();
    }
}
