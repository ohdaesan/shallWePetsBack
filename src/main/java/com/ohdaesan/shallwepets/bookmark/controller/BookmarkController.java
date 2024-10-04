package com.ohdaesan.shallwepets.bookmark.controller;

import com.ohdaesan.shallwepets.bookmark.domain.dto.BookmarkDTO;
import com.ohdaesan.shallwepets.bookmark.service.BookmarkService;
import com.ohdaesan.shallwepets.global.ResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "Bookmark")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    // 북마크 추가
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/bookmark")
    public ResponseEntity<ResponseDTO> createBookmark(@RequestBody BookmarkDTO bookmarkDTO) {
        try {
            BookmarkDTO createdBookmark = bookmarkService.createBookmark(bookmarkDTO);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("bookmark", createdBookmark);
            return ResponseEntity.ok().body(new ResponseDTO(201, "북마크 성공", responseMap));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDTO(409, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(500, "서버 오류", null));
        }
    }

    // 멤버 넘버로 모든 북마크 찾기
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{memberNo}")
    public ResponseEntity<ResponseDTO> getBookmarksByMember(@PathVariable Long memberNo) {
        try {
            List<BookmarkDTO> bookmarkList = bookmarkService.getBookmarksByMember(memberNo);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("bookmarks", bookmarkList);
            return ResponseEntity.ok(new ResponseDTO(200, "Bookmarks retrieved successfully", responseMap));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(404, "Member not found", null));
        }
    }

    // Delete a bookmark
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteBookmark(@RequestParam Long postNo, @RequestParam Long memberNo) {
        Optional<BookmarkDTO> deleteBookmark = bookmarkService.deleteBookmark(postNo, memberNo);

        Map<String, Object> responseMap = new HashMap<>();

        if (deleteBookmark.isEmpty()) {
            // Bookmark not found case
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(404, "해당 북마크를 삭제할 수 없습니다. 북마크가 되어있는지 확인해주세요.", responseMap));
        }

        // Bookmark deleted successfully case
        responseMap.put("bookmark", deleteBookmark.get());
        return ResponseEntity.ok(new ResponseDTO(200, "북마크 삭제 성공", responseMap));
    }

}
