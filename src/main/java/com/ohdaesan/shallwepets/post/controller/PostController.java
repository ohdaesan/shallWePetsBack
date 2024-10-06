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

//    @Operation(summary = "장소 리스트 조회", description = "특정 카테고리와 도시로 포스트 리스트 불러오기")
//    @GetMapping("/getList")
//    public ResponseEntity<ResponseDTO> getPostsByCategoryAndCities(
//            @RequestParam String category,
//            @RequestParam List<String> city) {
//        List<Post> posts = postService.getPostsByCategoryAndCities(category, city);
//
//        List<PostDTO> postDTOs = posts.stream()
//                .map(post -> modelMapper.map(post, PostDTO.class))
//                .collect(Collectors.toList());
//
//        Map<String, Object> responseMap = new HashMap<>();
//        responseMap.put("posts", postDTOs);
//
//        return ResponseEntity
//                .ok()
//                .body(new ResponseDTO(200, "포스트 리스트 조회 성공", responseMap));
//    }

    @Operation(summary = "장소 리스트 페이징 처리하여 조회", description = "특정 카테고리와 도시로 포스트 리스트 페이징 처리해서 불러오기")
    @GetMapping("/getList")
    public ResponseEntity<ResponseDTO> getPostsByCategoryAndCities(
            @RequestParam String category,
            @RequestParam List<String> city,
            @RequestParam int page) {
        List<Post> posts = postService.getPostsByCategoryAndCities(category, city, page);

        List<PostDTO> postDTOs = posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("posts", postDTOs);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(200, "포스트 리스트 조회 성공", responseMap));
    }

    @Operation(summary = "시군구 필터링된 장소 리스트 페이징 처리하여 조회", description = "특정 카테고리, 도시, 시군구로 포스트 리스트 페이징 처리해서 불러오기")
    @GetMapping("/getFilteredList")
    public ResponseEntity<ResponseDTO> getPostsByCategoryAndCitiesAndSigngu(
            @RequestParam String category,
            @RequestParam List<String> city,
            @RequestParam String signgu,
            @RequestParam int page) {
        List<Post> posts = postService.getPostsByCategoryAndCitiesAndSigngu(category, city, signgu, page);

        List<PostDTO> postDTOs = posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("posts", postDTOs);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(200, "포스트 리스트 조회 성공", responseMap));
    }

    @Operation(summary = "검색어 필터링된 장소 리스트 페이징 처리하여 조회", description = "특정 카테고리, 도시, 검색어로 포스트 리스트 페이징 처리해서 불러오기")
    @GetMapping("/getSearchedList")
    public ResponseEntity<ResponseDTO> getPostsByCategoryAndCitiesAndKeyword(
            @RequestParam String category,
            @RequestParam List<String> city,
            @RequestParam String keyword,
            @RequestParam int page) {
        List<Post> posts = postService.getPostsByCategoryAndCitiesAndKeyword(category, city, keyword, page);

        List<PostDTO> postDTOs = posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("posts", postDTOs);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(200, "포스트 리스트 조회 성공", responseMap));
    }

    @Operation(summary = "시군구와 검색어로 필터링된 장소 리스트 페이징 처리하여 조회", description = "특정 카테고리, 도시, 시군구, 검색어로 포스트 리스트 페이징 처리해서 불러오기")
    @GetMapping("/getFilteredSearchedList")
    public ResponseEntity<ResponseDTO> getPostsByCategoryAndCitiesAndSignguAndKeyword(
            @RequestParam String category,
            @RequestParam List<String> city,
            @RequestParam String signgu,
            @RequestParam String keyword,
            @RequestParam int page) {
        List<Post> posts = postService.getPostsByCategoryAndCitiesAndSignguAndKeyword(category, city, signgu, keyword, page);

        List<PostDTO> postDTOs = posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("posts", postDTOs);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(200, "포스트 리스트 조회 성공", responseMap));
    }

    @Operation(summary = "특정 도시의 구 리스트 조회", description = "주어진 도시에 따라 시군구 리스트 반환")
    @GetMapping("/getSigngu")
    public ResponseEntity<ResponseDTO> getDistinctSignguByCities(
            @RequestParam List<String> cities,
            @RequestParam String category) {
        List<String> signguList = postService.getDistinctSignguByCitiesAndCategory(cities, category);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("signguList", signguList);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(200, "시군구 리스트 조회 성공", responseMap));
    }
}
