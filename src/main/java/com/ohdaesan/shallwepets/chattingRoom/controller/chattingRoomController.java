package com.ohdaesan.shallwepets.chattingRoom.controller;

import com.ohdaesan.shallwepets.chattingRoom.service.ChattingRoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Chatting Room")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/chattingRoom")
public class chattingRoomController {
    private final ChattingRoomService chattingRoomService;


}
