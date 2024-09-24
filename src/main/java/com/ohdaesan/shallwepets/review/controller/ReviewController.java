package com.ohdaesan.shallwepets.review.controller;

import com.ohdaesan.shallwepets.global.ResponseDTO;
import com.ohdaesan.shallwepets.member.service.MemberService;
import com.ohdaesan.shallwepets.post.service.PostService;
import com.ohdaesan.shallwepets.review.domain.dto.ReviewDTO;
import com.ohdaesan.shallwepets.review.domain.entity.Review;
import com.ohdaesan.shallwepets.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "createReview", description = "업로드할 새로운 리뷰 작성")
    @PostMapping("/createReview")
    public ResponseEntity<ResponseDTO> createReview(@RequestBody ReviewDTO reviewDTO) {

        // ReviewService를 통해 리뷰 저장
        reviewService.createReview(reviewDTO);

        // 성공 메시지를 담은 응답 객체 생성
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("result", "리뷰 등록에 성공하였습니다.");

        // ResponseEntity를 사용해 HTTP 응답을 반환
        return ResponseEntity
                .ok()
                .body(new ResponseDTO(201, "리뷰 추가 성공", responseMap));

    }


    // 리뷰 전체 조회
    @Operation(summary = "searchAllReview", description = "리뷰 전체 조회")
    @GetMapping("/reviews")
    public ResponseEntity<ResponseDTO> getAllReviews() {
        List<ReviewDTO> reviewList = reviewService.getAllReviews();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("reviews", reviewList);

        return ResponseEntity.ok()
                .body(new ResponseDTO(200, "리뷰 조회 성공", responseMap));
    }

    // 단일조회
    @Operation(summary = "searchReviewById", description = "리뷰 단일 조회")
    @GetMapping("/{reviewNo}")
    public ResponseEntity<ResponseDTO> getReviewByNo(@PathVariable Long reviewNo) {
        ReviewDTO review = reviewService.getReviewByNo(reviewNo);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("review", review);

        return ResponseEntity.ok()
                .body(new ResponseDTO(200, "리뷰 조회 성공", responseMap));
    }


    // 리뷰 삭제
    @Operation(summary = "deleteReview", description = "리뷰 삭제")
    @DeleteMapping("/{reviewNo}")
    public ResponseEntity<ResponseDTO> deleteReview(@PathVariable Long reviewNo) {
        reviewService.deleteReview(reviewNo);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("result", "리뷰 삭제에 성공하였습니다.");

        return ResponseEntity.ok()
                .body(new ResponseDTO(200, "리뷰 삭제 성공", responseMap));
    }







}
