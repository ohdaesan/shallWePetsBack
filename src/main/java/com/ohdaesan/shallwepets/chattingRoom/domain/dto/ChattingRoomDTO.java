package com.ohdaesan.shallwepets.chattingRoom.domain.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChattingRoomDTO {

    private Long chattingRoomNo;
    private Long member1No;
    private Long member2No;
}
