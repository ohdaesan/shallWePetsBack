package com.ohdaesan.shallwepets.member.repository;

import com.ohdaesan.shallwepets.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByMemberId(String memberId);
}
