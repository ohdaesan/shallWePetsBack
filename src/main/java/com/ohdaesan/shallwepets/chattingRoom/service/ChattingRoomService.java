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

//    public Long createChattingRoom(Long member1No, Long member2No) {
//        Member member1 = memberService.findById(member1No);
//        Member member2 = memberService.findById(member2No);
//
//        // 새로운 채팅방 엔티티 생성
//        ChattingRoomEntity newChattingRoom = ChattingRoomEntity.builder()
//                .member1(member1)
//                .member2(member2)
//                .build();
//
//        // 채팅방 저장
//        ChattingRoomEntity savedChattingRoom = chattingRoomRepository.save(newChattingRoom);
//
//        return savedChattingRoom.getChattingRoomNo(); // 채팅방 번호 반환
//    }


    // 채팅방 하나만 불러오도록 생성
    // 기존 채팅방 찾기 또는 새 채팅방 생성
    public Long createOrFindChatRoom(Long memberNo, Long member2No) {
        // 1. 이미 존재하는 채팅방을 찾는다.
        List<ChattingRoomEntity> existingRoom = chattingRoomRepository.findByMember1_IdAndMember2_Id(memberNo, member2No);



        if (existingRoom != null) {
            // 채팅방이 이미 존재할 경우 그 채팅방을 반환
            return existingRoom.get(0).getChattingRoomNo();
        }

        // 2. 존재하지 않는다면 새로운 채팅방을 생성한다.
        Member member1 = memberService.findById(memberNo); // 첫 번째 멤버 조회
        Member member2 = memberService.findById(member2No); // 두 번째 멤버 조회

        ChattingRoomEntity newRoom = ChattingRoomEntity.builder()
                .member1(member1)
                .member2(member2)
                .status(ChattingRoomEntity.RoomStatus.ACTIVE)
                .createdDate(LocalDateTime.now())
                .build();

        ChattingRoomEntity savedChattingRoom = chattingRoomRepository.save(newRoom);

        return savedChattingRoom.getChattingRoomNo();
    }
}
