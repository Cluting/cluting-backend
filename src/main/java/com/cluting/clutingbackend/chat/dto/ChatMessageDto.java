package com.cluting.clutingbackend.chat.dto;

import lombok.Data;

@Data
public class ChatMessageDto {

    private String roomId;
    private String writer;
    private String message;
}
