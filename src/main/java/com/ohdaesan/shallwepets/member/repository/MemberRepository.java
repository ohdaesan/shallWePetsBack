package com.ohdaesan.shallwepets.member.repository;

import com.ohdaesan.shallwepets.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByMemberId(String memberId);
    Optional<Member> findMemberByMemberNameAndMemberEmail(String name, String email);
    Optional<Member> findMemberByMemberNameAndMemberPhone(String name, String phone);

    boolean existsByMemberIdAndMemberNameAndMemberEmail(String memberId, String name, String email);
    boolean existsByMemberIdAndMemberNameAndMemberPhone(String memberId, String name, String phone);

    Member findByMemberId(String memberId);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.memberPwd = :modifiedPw WHERE m.memberId = :memberId")
    int updateMemberPwByMemberId(String memberId, String modifiedPw);
}
