package com.ohdaesan.shallwepets.message.controller;

import com.ohdaesan.shallwepets.message.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Message")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;


}
