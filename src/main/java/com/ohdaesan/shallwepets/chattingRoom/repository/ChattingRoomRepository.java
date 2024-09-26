package com.ohdaesan.shallwepets.chattingRoom.repository;

import com.ohdaesan.shallwepets.chattingRoom.domain.entity.ChattingRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoomEntity, Long> {
}
