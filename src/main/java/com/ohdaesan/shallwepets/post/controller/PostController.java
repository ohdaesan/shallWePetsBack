package com.ohdaesan.shallwepets.post.controller;

import com.ohdaesan.shallwepets.global.ResponseDTO;
import com.ohdaesan.shallwepets.post.domain.dto.PostDTO;
import com.ohdaesan.shallwepets.post.domain.dto.PostSummaryDTO;
import com.ohdaesan.shallwepets.post.domain.entity.Post;
import com.ohdaesan.shallwepets.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "장소 리스트 모든 status 페이징 처리하여 조회", description = "포스트 리스트 모든 status 페이징 처리해서 불러오기")
    @GetMapping("/getListAdmin")
    public ResponseEntity<ResponseDTO> getPostsByCategoryAndCitiesAdmin(
            @RequestParam int page) {
        List<Post> posts = postService.getAllPostsAdmin(page);

        List<PostDTO> postDTOs = posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("posts", postDTOs);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(200, "포스트 리스트 조회 성공", responseMap));
    }

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

    // user의 업체 등록 신청
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "업체 등록", description = "user의 업체 등록")
    @PostMapping("/registerPost")
    public ResponseEntity<ResponseDTO> registerPost(@RequestBody PostDTO postDTO) {

        log.info(String.valueOf(postDTO));

        PostDTO post = postService.registerPost(postDTO);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("post", post);


        return ResponseEntity
                .ok()
                .body(new ResponseDTO(201, "업체 등록 신청 성공", responseMap));
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "post 페이징 조회", description = "post를 페이지별로 조회합니다.")
    @GetMapping("/getAllPost")
    public ResponseEntity<ResponseDTO> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "최신순") String sort, // 기본 정렬 기준 설정
            @RequestParam(required = false) String searchTerm // 검색어 추가
    ) {
        // Pageable 생성 (정렬 기준은 서비스에서 처리)
        Pageable pageable = PageRequest.of(page, size);

        // 서비스에서 페이징된 데이터와 검색어를 이용하여 필터링된 데이터를 가져옴
        Page<PostSummaryDTO> postPage = postService.getAllPost(pageable, searchTerm, sort);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("postList", postPage.getContent());
        responseMap.put("totalCount", postPage.getTotalElements());
        responseMap.put("totalPages", postPage.getTotalPages());

        return ResponseEntity.ok()
                .body(new ResponseDTO(200, "post 페이징 조회 성공", responseMap));
    }









    // 관리자의 폼 수정[반려 or 승인](상태 변경+ 반려사유)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "업체 상태 수정", description = "업체 상태 수정")
    @PutMapping("/update/{postNo}")
    public ResponseEntity<ResponseDTO> updatePostStatus(@PathVariable Long postNo, @RequestBody PostDTO postDTO) {
        PostDTO updatedPost = postService.updatePostStatus(postNo, postDTO);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("post", updatedPost);

        return ResponseEntity.ok()
                .body(new ResponseDTO(200, "게시물 수정 성공", responseMap));
    }



    // 신청자의 반려당한 이후 폼 수정
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "반려된 폼 수정", description = "반려된 폼 수정")
    @PutMapping("/rePost/{postNo}")
    public ResponseEntity<ResponseDTO> updatePostForm(@PathVariable Long postNo, @RequestBody PostDTO postDTO) {
        PostDTO updatedPost = postService.updatePostForm(postNo, postDTO);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("post", updatedPost);

        return ResponseEntity.ok()
                .body(new ResponseDTO(200, "폼 수정 성공", responseMap));
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Operation(summary = "post 조건 조회", description = "memberNo로 조회")
    @GetMapping("/getPostByMemberNo/{memberNo}")
    public ResponseEntity<ResponseDTO> getPostByMemberNo(@PathVariable Long memberNo) {
        List<PostDTO> postList = postService.getPostByMemberNo(memberNo);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("postList", postList);

        return ResponseEntity.ok()
                .body(new ResponseDTO(200, "post 전체 조회 성공", responseMap));
    }


    // 승인 신청 리스트 조회
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "post 조건 조회", description = "status가 APPROVED가 아닌 것들")
    @GetMapping("/getPostAwaitingList")
    public ResponseEntity<ResponseDTO> getPostAwaitingList() {
        // 서비스 호출
        List<PostDTO> postList = postService.getPostAwaitingList();

        // 응답 생성
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("postList", postList);

        return ResponseEntity.ok()
                .body(new ResponseDTO(200, "status가 APPROVED가 아닌 post 목록 조회 성공", responseMap));
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Operation(summary = "post 삭제", description = "삭제")
    @DeleteMapping("/delete/{postNo}")
    public ResponseEntity<ResponseDTO> deletePost(@PathVariable Long postNo) {
        // 서비스 호출하여 게시글 삭제
        Long deletedPostNo = postService.deletePost(postNo);

        // 응답 생성
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("deletedPostNo", deletedPostNo); // 삭제된 게시글 ID를 results에 포함

        return ResponseEntity.ok()
                .body(new ResponseDTO(200, "게시글이 성공적으로 삭제되었습니다.", responseMap));
    }



}