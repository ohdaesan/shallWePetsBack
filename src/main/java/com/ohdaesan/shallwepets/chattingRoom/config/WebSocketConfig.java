package com.ohdaesan.shallwepets.chattingRoom.config;

import com.ohdaesan.shallwepets.chattingRoom.handler.ChatWebSocketHandler;
import com.ohdaesan.shallwepets.chattingRoom.service.ChattingRoomService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;
    private final ChattingRoomService chattingRoomService;

    public WebSocketConfig(ChatWebSocketHandler chatWebSocketHandler, ChattingRoomService chattingRoomService) {
        this.chatWebSocketHandler = chatWebSocketHandler;
        this.chattingRoomService = chattingRoomService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        System.out.println("웹소켓핸들러 등록 자체가 되는지 안되는지 확인");
        registry.addHandler(chatWebSocketHandler, "/chat")
                // 웹소켓 모두를 허용하도록
                // /chat에서 일단 변경
                .setAllowedOrigins("*"); // 허용된 도메인 설정 - 일단 다 허용
                // withSockJS() 지운 상태  // SockJS 사용 가능하게 하는 코드

    }
}

