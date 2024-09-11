package com.ohdaesan.shallwepets.chattingRoom.service;

import com.ohdaesan.shallwepets.chattingRoom.repository.ChattingRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChattingRoomService {
    private final ChattingRoomRepository chattingRoomRepository;


}
