package com.ohdaesan.shallwepets.post.controller;

import com.ohdaesan.shallwepets.global.ResponseDTO;
import com.ohdaesan.shallwepets.post.domain.dto.PostDTO;
import com.ohdaesan.shallwepets.post.domain.entity.Post;
import com.ohdaesan.shallwepets.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Post")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final ModelMapper modelMapper;

    @Operation(summary = "특정 장소 정보 조회", description = "포스트 번호로 장소 정보 불러오기")
    @GetMapping("/{postNo}")
    public ResponseEntity<ResponseDTO> getPostDetails(@PathVariable Long postNo) {
        PostDTO post = postService.getPostDetails(postNo);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("post", post);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(201, "post 불러오기 성공", responseMap));
    }


    // user의 업체 등록 신청
//    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "업체 등록", description = "user의 업체 등록")
    @PostMapping("/registerPost")
    public ResponseEntity<ResponseDTO> registerPost(@RequestBody PostDTO postDTO) {
        PostDTO post = postService.registerPost(postDTO);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("post", post);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(201, "업체 등록 신청 성공", responseMap));
    }

    // 전체 폼 List 조회 (front에서 memberNo,awaiting)
    // 리뷰 전체 조회
//    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "post 전체 조회", description = "post 전체 조회")
    @GetMapping("/getAllPost")
    public ResponseEntity<ResponseDTO> getAllPost() {
        List<PostDTO> postList = postService.getAllPost();
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("postList", postList);

        return ResponseEntity.ok()
                .body(new ResponseDTO(200, "post 전체 조회 성공", responseMap));
    }

    // 관리자의 폼 수정[반려 or 승인](상태 변경+ 반려사유)
//    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "업체 상태 수정", description = "업체 상태 수정")
    @PutMapping("/{postNo}")
    public ResponseEntity<ResponseDTO> updatePostStatus(@PathVariable Long postNo, @RequestBody PostDTO postDTO) {
        PostDTO updatedPost = postService.updatePostStatus(postNo, postDTO);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("post", updatedPost);

        return ResponseEntity.ok()
                .body(new ResponseDTO(200, "게시물 수정 성공", responseMap));
    }


    // 신청자의 반려당한 이후 폼 수정
    @Operation(summary = "반려된 폼 수정", description = "반려된 폼 수정")
    @PutMapping("/rePost/{postNo}")
    public ResponseEntity<ResponseDTO> updatePostForm(@PathVariable Long postNo, @RequestBody PostDTO postDTO) {
        PostDTO updatedPost = postService.updatePostForm(postNo, postDTO);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("post", updatedPost);

        return ResponseEntity.ok()
                .body(new ResponseDTO(200, "폼 수정 성공", responseMap));
    }



}
