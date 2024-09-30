package com.ohdaesan.shallwepets.review.controller;

import com.ohdaesan.shallwepets.global.ResponseDTO;
import com.ohdaesan.shallwepets.member.service.MemberService;
import com.ohdaesan.shallwepets.post.service.PostService;
import com.ohdaesan.shallwepets.review.domain.dto.ReviewDTO;
import com.ohdaesan.shallwepets.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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


    // 단일조회(reviewNo)
    @Operation(summary = "searchReviewById", description = "리뷰 단일 조회")
    @GetMapping("/{reviewNo}")
    public ResponseEntity<ResponseDTO> getReviewByNo(@PathVariable Long reviewNo) {
        ReviewDTO review = reviewService.getReviewByNo(reviewNo);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("review", review);

        return ResponseEntity.ok()
                .body(new ResponseDTO(200, "리뷰 조회 성공", responseMap));
    }

    // 포스트 넘버로 모든 리뷰 조회
    @Operation(summary = "getReviewsByPostNo", description = "포스트 번호로 리뷰 조회 및 정렬")
    @GetMapping("/post/{postNo}")
    public ResponseEntity<ResponseDTO> getReviewsByPostNo(
            @PathVariable Long postNo,
            @RequestParam(value = "sortOrder", defaultValue = "recent") String sortOrder) { // sortOrder 추가
        log.info("Fetching reviews for postNo: {} with sortOrder: {}", postNo, sortOrder);

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
//    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "getReviewsByMemberNo", description = "회원 번호로 리뷰 조회")
    @GetMapping("/member/{memberNo}")
    public ResponseEntity<ResponseDTO> getReviewsByMemberNo(@PathVariable Long memberNo) {
        List<ReviewDTO> reviewList = reviewService.getReviewsByMemberNo(memberNo);

        int memberReviewCount = reviewList.size();
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("reviews", reviewList);
        responseMap.put("memberReviewCount", memberReviewCount);
        return ResponseEntity.ok(new ResponseDTO(200, "리뷰 조회 성공", responseMap));
    }


    @Operation(summary = "getAverageRateByPostNo", description = "포스트 번호로 리뷰의 평균 rate 조회")
    @GetMapping("/post/{postNo}/average-rate")
    public ResponseEntity<ResponseDTO> getAverageRateByPostNo(@PathVariable Long postNo) {
        log.info("Fetching average rate for postNo: {}", postNo);

        // 평균 rate 조회
        Double averageRate = reviewService.getAverageRateByPostNo(postNo);

        // 응답 데이터 생성
        return ResponseEntity.ok(new ResponseDTO(200, "평균 rate 조회 성공", averageRate));
    }


    // 리뷰 수정
//@PreAuthorize("hasAuthority('USER')")
    @PutMapping("/{reviewNo}")
    public ResponseEntity<ResponseDTO> updateReview(@PathVariable Long reviewNo, @RequestBody ReviewDTO reviewDTO) {
        System.out.println("🍔🍔🍔🍔🍔 reviewNo: " + reviewNo); // reviewNo 로그 추가
        System.out.println("🍔🍔🍔🍔🍔reviewDTO: " + reviewDTO);
        // reviewDTO의 rate와 content를 로그로 확인
        System.out.println("Rate: " + reviewDTO.getRate() + ", Content: " + reviewDTO.getContent());

        ReviewDTO updatedReview = reviewService.updateReview(reviewNo, reviewDTO);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("review", updatedReview);

        return ResponseEntity.ok()
                .body(new ResponseDTO(200, "리뷰 수정 성공", responseMap));
    }




    // 리뷰 삭제
//    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
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
