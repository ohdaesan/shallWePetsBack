package com.ohdaesan.shallwepets.chattingRoom.controller;

import com.ohdaesan.shallwepets.chattingRoom.service.ChattingRoomService;
import com.ohdaesan.shallwepets.message.domain.dto.MessageDTO;
import com.ohdaesan.shallwepets.message.domain.entity.MessageEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Long> createChattingRoom(@RequestBody Map<String, Long> members) {
        Long member1 = members.get("member1_no");
        Long member2 = members.get("member2_no");
        Long chattingRoomNo = chattingRoomService.createChattingRoom(member1, member2);

        // 생성된 채팅방 번호를 반환
        return ResponseEntity.ok(chattingRoomNo);
    }



}
