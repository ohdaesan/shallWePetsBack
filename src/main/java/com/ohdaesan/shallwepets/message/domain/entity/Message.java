package com.ohdaesan.shallwepets.message.domain.entity;

import com.ohdaesan.shallwepets.chattingRoom.domain.entity.ChattingRoom;
import com.ohdaesan.shallwepets.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageNo;

    @ManyToOne
    @JoinColumn(name = "chatting_room_no", nullable = false)
    private ChattingRoom chattingRoom;

    @ManyToOne
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;

    @Column(length = 1000)
    private String content;

    private LocalDateTime createdTime;
}
