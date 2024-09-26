package com.ohdaesan.shallwepets.chattingRoom.controller;

import com.ohdaesan.shallwepets.chattingRoom.service.ChattingRoomService;
import com.ohdaesan.shallwepets.message.domain.dto.MessageDTO;
import com.ohdaesan.shallwepets.message.domain.entity.MessageEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Chatting Room")
@RestController
@Slf4j
@RequestMapping("/chattingRoom")
public class ChattingRoomController {

    private final ChattingRoomService chattingRoomService;


    public ChattingRoomController(ChattingRoomService chattingRoomService) {

        this.chattingRoomService = chattingRoomService;
    }

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

}
