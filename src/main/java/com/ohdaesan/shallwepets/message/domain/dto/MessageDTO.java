package com.ohdaesan.shallwepets.message.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MessageDTO {
    private Long messageNo;
    private Long chattingRoomNo;
    private Long memberNo;
    private String content;
    private LocalDateTime createdTime;
}
