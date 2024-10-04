package com.ohdaesan.shallwepets.chattingRoom.controller;

import com.ohdaesan.shallwepets.chattingRoom.domain.entity.ChattingRoomEntity;
import com.ohdaesan.shallwepets.chattingRoom.service.ChattingRoomService;
import com.ohdaesan.shallwepets.message.domain.dto.MessageDTO;
import com.ohdaesan.shallwepets.message.domain.entity.MessageEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Chatting Room")
@RestController
@Slf4j
@RequestMapping("/chattingRoom")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
public class ChattingRoomController {

    private final ChattingRoomService chattingRoomService;


    public ChattingRoomController(ChattingRoomService chattingRoomService) {

        this.chattingRoomService = chattingRoomService;
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping("/message")
    public ResponseEntity<MessageEntity> sendMessage(@RequestBody MessageDTO messageDTO) {
        MessageEntity savedMessage = chattingRoomService.saveMessage(messageDTO.getMemberNo(), messageDTO.getChattingRoomNo(), messageDTO.getContent());

        return ResponseEntity.ok(savedMessage);

    }

    @GetMapping("/{chattingRoomNo}/messages")
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable Long chattingRoomNo) {
        List<MessageDTO> messages = chattingRoomService.getMessagesByChattingRoomNo(chattingRoomNo);
        return ResponseEntity.ok(messages);
    }

//    @PostMapping("/save")
//    public ResponseEntity<MessageEntity> saveMessage(
//            @RequestParam Long memberNo,
//            @RequestParam Long chattingRoomNo,
//            @RequestParam String content) {
//        MessageEntity message = chattingRoomService.saveMessage(memberNo, chattingRoomNo, content);
//        return ResponseEntity.ok(message);
//    }

    // 새로운 채팅방 생성
    @PostMapping("/create")
    public ResponseEntity<Map<String, Long>> createChattingRoom(@RequestBody Map<String, Long> members) {

        Long member1 = members.get("member1_no");
        Long member2 = members.get("member2_no");

        // 채팅룸 하나만 불러오도록 작성
        LocalDateTime createdDate = LocalDateTime.now().minusDays(0);
        ChattingRoomEntity.RoomStatus status = ChattingRoomEntity.RoomStatus.ACTIVE;


        // 로그 출력
        System.out.println("Received member1_no: " + member1);
        System.out.println("Received member2_no: " + member2);


//        Long chattingRoomNo = chattingRoomService.createChattingRoom(member1, member2);

        // 채팅룸 하나만 불러오도록 생성
        Long chattingRoomNo = chattingRoomService.createOrFindChatRoom(member1, member2);

        // 생성된 채팅방 번호를 반환
//        return ResponseEntity.ok(chattingRoomNo);
        Map<String, Long> response = new HashMap<>();
        response.put("chattingRoomNo", chattingRoomNo);
        return ResponseEntity.ok(response);
    }



}
