package com.cluting.clutingbackend.global.message;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageUtil {

    private final DefaultMessageService messageService;
    private final String from;

    public MessageUtil(
            @Value("${coolsms.api.key}") String apiKey,
            @Value("${coolsms.api.secret}") String apiSecret,
            @Value("${coolsms.from.number}") String from
    ) {
        this.from = from;
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    public void send(String to, String msg) {
        to = to.replaceAll("-", "");
        if (!to.startsWith("010")) {
            return;
        }
        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        message.setText(msg);

        return;

//        this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }
}
