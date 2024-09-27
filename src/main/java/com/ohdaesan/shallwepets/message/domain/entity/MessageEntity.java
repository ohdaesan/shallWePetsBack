package com.ohdaesan.shallwepets.message.domain.entity;

import com.ohdaesan.shallwepets.chattingRoom.domain.entity.ChattingRoomEntity;
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
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageNo;

    @ManyToOne
    @JoinColumn(name = "chatting_room_no", nullable = false)
    private ChattingRoomEntity chattingRoom;

    @ManyToOne
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;

    @Column(length = 1000)
    private String content;

    private LocalDateTime createdTime;

    // Member의 memberNo를 반환하는 메서드 추가
    public Long getMemberNo() {
        return member != null ? member.getMemberNo() : null; // null 체크
    }
}
