package com.ohdaesan.shallwepets.member.repository;

import com.ohdaesan.shallwepets.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByMemberId(String memberId);
    Optional<Member> findMemberByMemberNo(Long memberNo);
    Optional<Member> findMemberByMemberNameAndMemberEmail(String name, String email);
    Optional<Member> findMemberByMemberNameAndMemberPhone(String name, String phone);

    boolean existsByMemberId(String memberId);
    boolean existsByMemberNickname(String memberNickname);
    boolean existsByMemberEmail(String memberEmail);
    boolean existsByMemberPhone(String memberPhone);
    boolean existsByMemberIdAndMemberNameAndMemberEmail(String memberId, String name, String email);
    boolean existsByMemberIdAndMemberNameAndMemberPhone(String memberId, String name, String phone);

    Member findByMemberId(String memberId);

    Member findByMemberNo(Long memberNo);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.memberPwd = :modifiedPw WHERE m.memberId = :memberId")
    int updateMemberPwByMemberId(String memberId, String modifiedPw);

    List<Member> findByImage_ImageNo(Long imageNo);

//    Long findImageNoByMemberNo(Long memberNo);
    @Query("SELECT m.image.imageNo FROM Member m WHERE m.memberNo = :memberNo")
    Long findImageNoByMemberNo(@Param("memberNo") Long memberNo);

    Optional<Member> findPostByMemberNo(Long memberNo);

}

