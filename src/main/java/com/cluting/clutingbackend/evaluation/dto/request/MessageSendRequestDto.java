package com.cluting.clutingbackend.evaluation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageSendRequestDto {
    private List<Content> list;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Content {
        private String message;
        private String phone;
    }
}
