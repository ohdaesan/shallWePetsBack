package com.ohdaesan.shallwepets.review.controller;

import com.ohdaesan.shallwepets.global.ResponseDTO;
import com.ohdaesan.shallwepets.member.service.MemberService;
import com.ohdaesan.shallwepets.post.service.PostService;
import com.ohdaesan.shallwepets.review.domain.dto.ReviewDTO;
import com.ohdaesan.shallwepets.review.domain.dto.ReviewUpdateRequestDTO;
import com.ohdaesan.shallwepets.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Review")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final PostService postService;
    private final MemberService memberService;

    // 리뷰 작성(Create)
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "리뷰 작성", description = "업로드할 새로운 리뷰 작성")
    @PostMapping("/createReview")
    public ResponseEntity<ResponseDTO> createReview(@RequestBody ReviewDTO reviewDTO) {

        // ReviewService를 통해 리뷰 저장
        Long reviewNo = reviewService.createReview(reviewDTO);

        // 성공 메시지를 담은 응답 객체 생성
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("result", "리뷰 등록에 성공하였습니다.");
        responseMap.put("reviewNo", reviewNo);

        // ResponseEntity를 사용해 HTTP 응답을 반환
        return ResponseEntity
                .ok()
                .body(new ResponseDTO(201, "리뷰 추가 성공", responseMap));

    }

    // 리뷰 전체 조회
    @Operation(summary = "리뷰 전체 조회", description = "리뷰 전체 조회")
    @GetMapping("/reviews")
    public ResponseEntity<ResponseDTO> getAllReviews() {
        List<ReviewDTO> reviewList = reviewService.getAllReviews();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("reviews", reviewList);

        return ResponseEntity.ok()
                .body(new ResponseDTO(200, "리뷰 조회 성공", responseMap));
    }

    // 단일조회(reviewNo)
    @Operation(summary = "리뷰 단일 조회", description = "리뷰 넘버로 리뷰 조회")
    @GetMapping("/{reviewNo}")
    public ResponseEntity<ResponseDTO> getReviewByNo(@PathVariable Long reviewNo) {
        ReviewDTO review = reviewService.getReviewByNo(reviewNo);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("review", review);

        return ResponseEntity.ok()
                .body(new ResponseDTO(200, "리뷰 조회 성공", responseMap));
    }

    // 포스트 넘버로 모든 리뷰 조회
    @Operation(summary = "리뷰 전체 조회", description = "포스트 번호로 리뷰 전체 조회 및 정렬")
    @GetMapping("/post/{postNo}")
    public ResponseEntity<ResponseDTO> getReviewsByPostNo(
            @PathVariable Long postNo,
            @RequestParam(value = "sortOrder", defaultValue = "recent") String sortOrder) { // sortOrder 추가

        // 리뷰 목록 조회
        List<ReviewDTO> reviewList = reviewService.getReviewsByPostNo(postNo, sortOrder); // 정렬 기준 전달

        // 리뷰 총 개수 계산
        int reviewCount = reviewList.size();

        // 응답 데이터 생성
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("reviews", reviewList);        // 리뷰 목록 추가
        responseMap.put("reviewCount", reviewCount);   // 총 리뷰 개수 추가

        return ResponseEntity.ok(new ResponseDTO(200, "리뷰 조회 성공", responseMap));
    }

    // MemberNo로 리뷰 조회
    @Operation(summary = "회원 번호로 리뷰 조회", description = "회원 번호로 리뷰 조회")
    @GetMapping("/member/{memberNo}")
    public ResponseEntity<ResponseDTO> getReviewsByMemberNo(@PathVariable Long memberNo) {
        List<ReviewDTO> reviewList = reviewService.getReviewsByMemberNo(memberNo);

        int memberReviewCount = reviewList.size();
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("reviews", reviewList);
        responseMap.put("memberReviewCount", memberReviewCount);
        return ResponseEntity.ok(new ResponseDTO(200, "리뷰 조회 성공", responseMap));
    }

    @Operation(summary = "평균 별점 조회", description = "포스트 번호로 리뷰의 평균 별점 조회")
    @GetMapping("/post/{postNo}/average-rate")
    public ResponseEntity<ResponseDTO> getAverageRateByPostNo(@PathVariable Long postNo) {
        // 평균 rate 조회
        Double averageRate = reviewService.getAverageRateByPostNo(postNo);

        // 응답 데이터 생성
        return ResponseEntity.ok(new ResponseDTO(200, "평균 rate 조회 성공", averageRate));
    }

    // 리뷰 수정
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "리뷰 수정", description = "리뷰 번호로 리뷰 수정")
    @PutMapping("/update/{reviewNo}")
    public ResponseEntity<ResponseDTO> updateReview(
            @PathVariable Long reviewNo,
            @RequestBody ReviewUpdateRequestDTO reviewUpdateRequestDTO){
        ReviewDTO reviewDTO = reviewUpdateRequestDTO.getReviewDTO();
        List<Long> imagesToRemove = reviewUpdateRequestDTO.getImagesToRemove();

        ReviewDTO updatedReview = reviewService.updateReview(reviewNo, reviewDTO, imagesToRemove);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("review", updatedReview);

        return ResponseEntity.ok()
                .body(new ResponseDTO(200, "리뷰 수정 성공", responseMap));
    }

    // 리뷰 삭제
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "리뷰 삭제", description = "리뷰 번호로 리뷰 삭제")
    @DeleteMapping("/delete/{reviewNo}")
    public ResponseEntity<ResponseDTO> deleteReview(@PathVariable Long reviewNo) {
        reviewService.deleteReview(reviewNo);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("result", "리뷰 삭제에 성공하였습니다.");

        return ResponseEntity.ok()
                .body(new ResponseDTO(200, "리뷰 삭제 성공", responseMap));
    }

    // 특정 포스트에 대한 리뷰 수 가져오기
    @GetMapping("/count/{postNo}")
    public ResponseEntity<Integer> getReviewCountByPostNo(@PathVariable Long postNo) {
        int reviewCount = reviewService.getReviewCountByPostNo(postNo);
        return ResponseEntity.ok(reviewCount);
    }

    // 특정 포스트에 대한 평균 평점 가져오기
    @GetMapping("/averageRate/{postNo}")
    public ResponseEntity<Double> getAverageRate(@PathVariable Long postNo) {
        double averageRate = reviewService.getAverageRateByPostNo(postNo);
        return ResponseEntity.ok(averageRate);
    }
}
