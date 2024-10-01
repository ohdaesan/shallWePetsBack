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

    @Operation(summary = "장소 리스트 조회", description = "특정 카테고리와 도시로 포스트 리스트 불러오기")
    @GetMapping("/getList")
    public ResponseEntity<ResponseDTO> getPostsByCategoryAndCities(
            @RequestParam String category,
            @RequestParam List<String> city) {
        List<Post> posts = postService.getPostsByCategoryAndCities(category, city);

        List<PostDTO> postDTOs = posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("posts", postDTOs);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(200, "포스트 리스트 조회 성공", responseMap));
    }
}
