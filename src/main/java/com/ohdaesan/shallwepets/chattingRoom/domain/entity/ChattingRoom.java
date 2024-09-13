package com.ohdaesan.shallwepets.chattingRoom.domain.entity;

import com.ohdaesan.shallwepets.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chatting_room")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ChattingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chattingRoomNo;

    @ManyToOne
    @JoinColumn(name = "member1_no", nullable = false)
    private Member member1;

    @ManyToOne
    @JoinColumn(name = "member2_no", nullable = false)
    private Member member2;
}
