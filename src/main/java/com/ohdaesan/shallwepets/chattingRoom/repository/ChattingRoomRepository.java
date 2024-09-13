package com.ohdaesan.shallwepets.chattingRoom.repository;

import com.ohdaesan.shallwepets.chattingRoom.domain.entity.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {
}
