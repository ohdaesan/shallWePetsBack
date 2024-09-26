package com.ohdaesan.shallwepets.message.repository;

import com.ohdaesan.shallwepets.message.domain.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findByChattingRoom_ChattingRoomNo(Long ChattingRoomNo);
}
