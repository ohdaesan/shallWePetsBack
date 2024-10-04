package com.ohdaesan.shallwepets.chattingRoom.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohdaesan.shallwepets.auth.util.TokenUtils;
import com.ohdaesan.shallwepets.chattingRoom.service.ChattingRoomService;
import com.ohdaesan.shallwepets.message.domain.dto.MessageDTO;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.ohdaesan.shallwepets.auth.util.TokenUtils.isValidToken;

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
        System.out.println("afterConnectionEstablished called");
//        sessions.put(session.getId(), session); // 세션 저장
//        System.out.println("새로운 WebSocket 연결: " + session.getId());

        System.out.println("Session ID: " + session.getId());
        URI sessionUri = session.getUri();
        System.out.println("Session URI: " + sessionUri); // URI 로그 출력

        // 쿼리 이용해서 토큰 받아오기 구현
        String token = extractToken(Objects.requireNonNull(session.getUri()));
        //                          (session.getUri()); 에서 수정
        System.out.println("추출된 토큰:" + token); // 토큰이 잘 추출되는지 확인하기 위한 로그



        if (token == null || !TokenUtils.isValidToken(token)) {
            session.close(CloseStatus.POLICY_VIOLATION.withReason("토큰이 없습니다"));
            return;
        }



        // 토큰에서 memberNo 추출하기
//        Claims claims = TokenUtils.getClaimsFromToken(token);
//        Long memberNo = claims.get("memberNo", Long.class);
//        if (memberNo == null) {
//            session.close(CloseStatus.POLICY_VIOLATION.withReason("Member ID not found in token"));
//            return;
//        }
        Long memberNo = null;
        try {
            Claims claims = TokenUtils.getClaimsFromToken(token);
            memberNo = claims.get("memberNo", Long.class);
            System.out.println("추출된 memberNo: " + memberNo);

            if (memberNo == null) {
                System.out.println("memberNo를 찾을 수 없음");
                session.close(CloseStatus.POLICY_VIOLATION.withReason("Member ID not found in token"));
                return;
            }
        } catch (Exception e) {
            System.out.println("토큰 처리 중 예외 발생: " + e.getMessage());
            session.close(CloseStatus.POLICY_VIOLATION.withReason("Token processing error"));
            return;
        }


//
        // memberNo 저장하기
        System.out.println("세션 속성 설정 전: " + session.getAttributes());
        session.getAttributes().put("memberNo", memberNo);
        System.out.println("세션 속성 설정 후: " + session.getAttributes());

        System.out.println("세션 저장 전: 세션 ID = " + session.getId());
        sessions.put(session.getId(), session);
        System.out.println("세션 저장 후: " + sessions);
        System.out.println("새로운 WebSocket 연결: " + session.getId() + ", Member No: " + memberNo);

        if (session.isOpen()) {
            System.out.println("세션이 열려있음: " + session.getId());
        } else {
            System.out.println("세션이 닫혀 있음: " + session.getId());
        }

    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        // 메시지 DTO 파싱
        if (payload.startsWith("{") && payload.endsWith("}")) {
            Map<String, Long> memberIds = objectMapper.readValue(payload, new TypeReference<Map<String, Long>>() {
            });
            Long member1No = memberIds.get("member1_no");
            Long member2No = memberIds.get("member2_no");

            // 채팅방 생성
            Long chattingRoomNo = chattingRoomService.createChattingRoom(member1No, member2No);
            session.sendMessage(new TextMessage("새로운 채팅방 생성됨: " + chattingRoomNo));
        } else {
            // 기존 메시지 처리 로직
            MessageDTO messageDTO = objectMapper.readValue(payload, MessageDTO.class);
            chattingRoomService.saveMessage(messageDTO.getMemberNo(), messageDTO.getChattingRoomNo(), messageDTO.getContent());

            // 연결된 모든 사람에게 메시지 보여주기
            for (WebSocketSession wsSession : sessions.values()) {
                if (wsSession.isOpen()) {
                    wsSession.sendMessage(new TextMessage(payload));
                }
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

//    private String extractToken(WebSocketSession session) {
//        Map<String, Object> attributes = session.getAttributes();
//        String header = (String) attributes.get("Authorization");
//        return TokenUtils.splitHeader(header);
//    }

    // URI에서 쿼리 파라미터에서 토큰 추출
    private String extractToken(URI uri) {
        if (uri != null) {
            String query = uri.getQuery();
            if (query != null) {
                String[] params = query.split("&");
                for (String param : params) {
                    String[] pair = param.split("=");
                    if (pair.length == 2 && "token".equals(pair[0])) {
                        return pair[1]; // 토큰 값 반환
                    }
                }
            }
        }
        return null; // 토큰이 없을 경우 null 반환
    }
}
