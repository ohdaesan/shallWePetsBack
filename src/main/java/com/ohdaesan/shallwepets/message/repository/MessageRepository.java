package com.ohdaesan.shallwepets.message.repository;

import com.ohdaesan.shallwepets.message.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
}
