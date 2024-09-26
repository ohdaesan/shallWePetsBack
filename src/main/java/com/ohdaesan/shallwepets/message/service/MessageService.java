package com.ohdaesan.shallwepets.message.service;

import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.message.domain.entity.MessageEntity;
import com.ohdaesan.shallwepets.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;

    // 메시지 ID로 조회하는 메서드
    public MessageEntity findById(Long messageNo) {
        return messageRepository.findById(messageNo)
                .orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다."));
    }

    // 메시지를 저장하는 메서드 추가
    public MessageEntity saveMessage(MessageEntity message) {
        return messageRepository.save(message);
    }

    // 채팅방 번호로 메시지 리스트 가져오기
    public List<MessageEntity> findByChattingRoom_ChattingRoomNo(Long chattingRoomNo) {
        return messageRepository.findByChattingRoom_ChattingRoomNo(chattingRoomNo);
    }



}
