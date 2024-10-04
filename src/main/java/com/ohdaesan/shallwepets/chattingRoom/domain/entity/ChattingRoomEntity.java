package com.ohdaesan.shallwepets.chattingRoom.domain.entity;

import com.ohdaesan.shallwepets.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chatting_room")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChattingRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chattingRoomNo;

    @ManyToOne
    @JoinColumn(name = "member1_no", nullable = false)
    private Member member1;

    @ManyToOne
    @JoinColumn(name = "member2_no", nullable = false)
    private Member member2;

    // 생성일 추가
    private java.time.LocalDateTime createdDate;

    // 활성화 상태 추가
    public enum RoomStatus {
        ACTIVE,
        INACTIVE
    }

    @Enumerated(EnumType.STRING)
    @Builder.Default // 기본값 설정
    private RoomStatus status = RoomStatus.ACTIVE;

    // 생성자에서 createdDate 초기화
    @PrePersist
    private void prePersist() {
        createdDate = LocalDateTime.now();
    }
}
