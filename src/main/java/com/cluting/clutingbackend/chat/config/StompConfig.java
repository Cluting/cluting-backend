package com.cluting.clutingbackend.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class StompConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        // stomp 접속 주소 url -> ws://AWS EC2 ip주소/ws
        registry.addEndpoint("/stomp/chat")
                .setAllowedOrigins("http://*:8081")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        // 메시지를 수신하는 요청 엔드포인트
//        registry.enableSimpleBroker("/sub");
        // 메시지를 송신하는 엔드포인트
//        registry.setApplicationDestinationPrefixes("/pub");

        registry.setApplicationDestinationPrefixes("/pub");
        registry.setPathMatcher(new AntPathMatcher(".")); // url을 chat/room/3 -> chat.room.3으로 참조하기 위한 설정

        registry.enableStompBrokerRelay("/queue","/topic","/exchange","amq/queue");

    }
}
