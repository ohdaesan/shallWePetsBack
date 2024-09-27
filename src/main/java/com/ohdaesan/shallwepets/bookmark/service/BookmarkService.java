package com.ohdaesan.shallwepets.bookmark.service;

import com.ohdaesan.shallwepets.bookmark.domain.dto.BookmarkDTO;
import com.ohdaesan.shallwepets.bookmark.domain.entity.Bookmark;
import com.ohdaesan.shallwepets.bookmark.repository.BookmarkRepository;
import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.member.repository.MemberRepository;
import com.ohdaesan.shallwepets.post.domain.entity.Post;
import com.ohdaesan.shallwepets.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public BookmarkDTO createBookmark(BookmarkDTO bookmarkDTO) {
        // member와 post 엔티티 가져오기
        Member member = memberRepository.findById(bookmarkDTO.getMemberNo())
                .orElseThrow(() -> new IllegalStateException("로그인 후 이용해주세요"));
        Post post = postRepository.findById(bookmarkDTO.getPostNo())
                .orElseThrow(() -> new IllegalArgumentException("해당 post가 없습니다"));

        // 해당 memberNo와 postNo에 대한 기존 북마크 확인
        boolean exists = bookmarkRepository.existsByMemberAndPost(member, post);
//        (bookmarkDTO.getMemberNo(), bookmarkDTO.getPostNo());
        if (exists) {
            throw new IllegalStateException("이미 북마크했습니다.");
        }

        // 새로운 북마크 생성
        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .post(post)
                .createdDate(LocalDateTime.now())
                .build();

        // 북마크 저장
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        // DTO로 변환하여 반환
        return mapToDTO(savedBookmark);
    }


    // 멤버로 북마크 조회
    public List<BookmarkDTO> getBookmarksByMember(Long memberNo) {
        // memberNo로 북마크 목록 조회
//        List<Bookmark> bookmarks = bookmarkRepository.findByMember(memberNo); // 회원에 따른 북마크 목록을 조회하는 메서드가 필요합니다.

        List<Bookmark> bookmarks = bookmarkRepository.findByMember_MemberNo(memberNo);
        // 북마크 DTO 목록으로 변환하여 반환
        return bookmarks.stream()
                .map(bookmark -> new BookmarkDTO(
                        bookmark.getBookmarkNo(),
                        bookmark.getMember().getMemberNo(),
                        bookmark.getPost().getPostNo(),
                        bookmark.getCreatedDate()
                ))
                .collect(Collectors.toList());
    }



    // Helper method to map Bookmark entity to DTO
    private BookmarkDTO mapToDTO(Bookmark bookmark) {
        return new BookmarkDTO(
                bookmark.getBookmarkNo(),
                bookmark.getMember().getMemberNo(),
                bookmark.getPost().getPostNo(),
                bookmark.getCreatedDate()
        );

    }

    @Transactional
    public Optional<BookmarkDTO> deleteBookmark(Long postNo, Long memberNo) {
        Optional<Bookmark> bookmark = bookmarkRepository.findByPost_PostNoAndMember_MemberNo(postNo, memberNo);

        if (bookmark.isPresent()) {
            bookmarkRepository.delete(bookmark.get());
            BookmarkDTO deletedBookmarkDTO = new BookmarkDTO(
                    bookmark.get().getBookmarkNo(),
                    bookmark.get().getMember().getMemberNo(),
                    bookmark.get().getPost().getPostNo(),
                    bookmark.get().getCreatedDate()
            );
            return Optional.of(deletedBookmarkDTO);
        } else {
            return Optional.empty();
        }
    }

}
