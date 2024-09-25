package com.ohdaesan.shallwepets.post.controller;

import com.ohdaesan.shallwepets.global.ResponseDTO;
import com.ohdaesan.shallwepets.post.domain.dto.PostDTO;
import com.ohdaesan.shallwepets.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Post")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @Operation(summary = "connectPost", description = "포스트 api 연동할 코드")
    @GetMapping("/{postNo}")
    public ResponseEntity<ResponseDTO> getPostDetails(@PathVariable Long postNo) {
        PostDTO post = postService.getPostDetails(postNo);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("post", post);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(201, "post 불러오기 성공", responseMap));

    }
}
