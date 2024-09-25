package com.ohdaesan.shallwepets.bookmark.repository;

import com.ohdaesan.shallwepets.bookmark.domain.entity.Bookmark;
import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    boolean existsByMemberAndPost(Member member, Post post);

    List<Bookmark> findByMember_MemberNo(Long memberNo);

    Optional<Bookmark> findByPost_PostNoAndMember_MemberNo(Long postNo, Long memberNo);
}
