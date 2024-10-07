package com.ohdaesan.shallwepets.post.repository;

import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByMember(Member member);

    Optional<Object> findByMemberAndPostNo(Member member, Long postNo);

    Optional<Object> findByPostNoAndMemberMemberNo(Long postNo, Long memberNo);

    List<Post> findByMember_MemberNo(Long memberNo);
}
