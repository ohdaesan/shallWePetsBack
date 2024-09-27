package com.ohdaesan.shallwepets.chattingRoom.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohdaesan.shallwepets.chattingRoom.service.ChattingRoomService;
import com.ohdaesan.shallwepets.message.domain.dto.MessageDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    // 현재 연결된 모든 세션을 저장할 맵
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ChattingRoomService chattingRoomService;

    public ChatWebSocketHandler(ChattingRoomService chattingRoomService) {
        this.chattingRoomService = chattingRoomService;
    }

    // 클라이언트가 연결될 때 호출
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session); // 세션 저장
        System.out.println("새로운 WebSocket 연결: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        MessageDTO messageDTO = objectMapper.readValue(payload, MessageDTO.class); // 메시지 DTO 파싱

        // 메시지를 저장
        chattingRoomService.saveMessage(messageDTO.getMemberNo(), messageDTO.getChattingRoomNo(), messageDTO.getContent());

        // 연결된 모든 클라이언트에게 브로드캐스트
        for (WebSocketSession wsSession : sessions.values()) {
            if (wsSession.isOpen()) {
                wsSession.sendMessage(new TextMessage(payload));
            }
        }
    }

    // 연결이 종료될 때 호출
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId()); // 세션 제거
        System.out.println("WebSocket 연결 종료: " + session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close(CloseStatus.SERVER_ERROR); // 에러 발생 시 세션 닫기
        }
        sessions.remove(session.getId());
        System.err.println("WebSocket 에러: " + exception.getMessage());
    }
}
