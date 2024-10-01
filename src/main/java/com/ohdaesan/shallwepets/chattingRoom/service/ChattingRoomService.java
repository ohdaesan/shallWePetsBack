package com.ohdaesan.shallwepets.chattingRoom.service;

import com.ohdaesan.shallwepets.chattingRoom.domain.entity.ChattingRoomEntity;
import com.ohdaesan.shallwepets.chattingRoom.repository.ChattingRoomRepository;
import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.member.service.MemberService;
import com.ohdaesan.shallwepets.message.domain.dto.MessageDTO;
import com.ohdaesan.shallwepets.message.domain.entity.MessageEntity;
import com.ohdaesan.shallwepets.message.repository.MessageRepository;
import com.ohdaesan.shallwepets.message.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChattingRoomService {
    private final ChattingRoomRepository chattingRoomRepository;
    private final MessageService messageService;
    private final MemberService memberService;

    public ChattingRoomService(ChattingRoomRepository chattingRoomRepository, MessageService messageService, MemberService memberService) {
        this.chattingRoomRepository = chattingRoomRepository;
        this.messageService = messageService;
        this.memberService = memberService;
    }


    // 메시지를 저장하는 메서드
    public MessageEntity saveMessage(Long memberNo, Long chattingRoomNo, String content) {
        ChattingRoomEntity chattingRoom = chattingRoomRepository.findById(chattingRoomNo)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        Member member = memberService.findById(memberNo);

        MessageEntity message = MessageEntity.builder()
                .chattingRoom(chattingRoom)
                .member(member) // memberNo를 메시지에 추가
                .content(content)
                .createdTime(LocalDateTime.now())
                .build();

        return messageService.saveMessage(message); // MessageService를 사용하여 메시지를 저장
    }



    // 채팅방 번호로 메시지 리스트 가져오기
    public List<MessageDTO> getMessagesByChattingRoomNo(Long chattingRoomNo) {
        return messageService.findByChattingRoom_ChattingRoomNo(chattingRoomNo)
                .stream()
                .map(messageEntity -> new MessageDTO(
                        messageEntity.getMessageNo(),
                        messageEntity.getChattingRoom().getChattingRoomNo(),
                        messageEntity.getMemberNo(),
                        messageEntity.getContent(),
                        messageEntity.getCreatedTime()
                ))
                .collect(Collectors.toList());
    }

    public Long createChattingRoom(Long member1No, Long member2No) {
        Member member1 = memberService.findById(member1No);
        Member member2 = memberService.findById(member2No);

        // 새로운 채팅방 엔티티 생성
        ChattingRoomEntity newChattingRoom = ChattingRoomEntity.builder()
                .member1(member1)
                .member2(member2)
                .build();

        // 채팅방 저장
        ChattingRoomEntity savedChattingRoom = chattingRoomRepository.save(newChattingRoom);

        return savedChattingRoom.getChattingRoomNo(); // 채팅방 번호 반환
    }
}
