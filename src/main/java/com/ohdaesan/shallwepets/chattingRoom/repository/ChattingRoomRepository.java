package com.ohdaesan.shallwepets.chattingRoom.repository;

import com.ohdaesan.shallwepets.chattingRoom.domain.entity.ChattingRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoomEntity, Long> {

    // 채팅방을 하나만 반환하기 위한 수정
    @Query("SELECT c FROM ChattingRoomEntity c WHERE (c.member1.memberNo = :member1Id AND c.member2.memberNo = :member2Id) OR (c.member1.memberNo = :member2Id AND c.member2.memberNo = :member1Id)")
    List<ChattingRoomEntity> findByMember1_IdAndMember2_Id(@Param("member1Id") Long member1Id, @Param("member2Id") Long member2Id);
    //    ChattingRoomEntity findByMember1_IdAndMember2_Id(Long memberNo, Long member2No);

//    // 특정 멤버가 속한 모든 채팅방 조회
//    @Query("SELECT c FROM ChattingRoomEntity c WHERE c.member1.memberNo = :memberNo OR c.member2.memberNo = :memberNo")
//    List<ChattingRoomEntity> findByMemberNo(@Param("memberNo") Long memberNo);

}
