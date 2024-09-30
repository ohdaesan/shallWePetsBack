package com.ohdaesan.shallwepets.review.service;

import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.member.repository.MemberRepository;
import com.ohdaesan.shallwepets.point.domain.entity.Point;
import com.ohdaesan.shallwepets.point.repository.PointRepository;
import com.ohdaesan.shallwepets.post.domain.entity.Post;
import com.ohdaesan.shallwepets.post.repository.PostRepository;
import com.ohdaesan.shallwepets.review.domain.dto.ReviewDTO;
import com.ohdaesan.shallwepets.review.domain.entity.Review;
import com.ohdaesan.shallwepets.review.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper; // ModelMapper 주입
    private final PointRepository pointRepository;


    @Transactional
    public void createReview(ReviewDTO reviewDTO) {
        log.info("seveice 옴");
        // Member와 Post는 외부에서 매핑된 엔티티이므로, ID를 통해 직접 조회해 설정해줍니다.
        Member member = memberRepository.findById(reviewDTO.getMemberNo())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        Post post = postRepository.findById(reviewDTO.getPostNo())
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        // Review 엔티티 빌더 패턴으로 생성
        Review review = Review.builder()
                .member(member)        // 조회된 Member 엔티티 설정
                .post(post)            // 조회된 Post 엔티티 설정
                .rate(reviewDTO.getRate())      // DTO에서 가져온 평점 설정
                .content(reviewDTO.getContent()) // DTO에서 가져온 리뷰 내용 설정
                .createdDate(LocalDateTime.now()) // 현재 시간으로 생성 시간 설정
                .modifiedDate(null)     // 처음 생성 시 수정 날짜는 null
                .build();

        reviewRepository.save(review);
        log.info("Review saved: {}", review);

        // Review 엔티티를 데이터베이스에 저장
        Point point = Point.builder()
                .member(review.getMember())
                .point(10)
                .createdDate(LocalDateTime.now())
                .comment("Review submitted, points awarded")
                .build();

        log.info("여기까지 왔니?");
        // Save the points
        pointRepository.save(point);

    }



    @Transactional(readOnly = true)
    public List<ReviewDTO> getAllReviews() {
        // 데이터베이스에서 모든 리뷰 조회
        List<Review> reviews = reviewRepository.findAll();

        return reviews.stream()
                .map(review -> {
                    ReviewDTO dto = new ReviewDTO();
                    dto.setReviewNo(review.getReviewNo());
                    dto.setRate(review.getRate());
                    dto.setContent(review.getContent());
                    dto.setCreatedDate(review.getCreatedDate());
                    dto.setMemberNo(review.getMember().getMemberNo()); // 필요시 memberNo 포함
                    dto.setPostNo(review.getPost().getPostNo()); // 필요시 postNo 포함
                    return dto;
                })
                .collect(Collectors.toList());
    }


    // 리뷰 넘버로 조회
    @Transactional(readOnly = true)
    public ReviewDTO getReviewByNo(Long reviewNo) {
        Review review = reviewRepository.findById(reviewNo)
                .orElseThrow(() -> new EntityNotFoundException(" 리뷰가 없습니다."));

        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setPostNo(review.getPost().getPostNo());
        reviewDTO.setReviewNo(review.getReviewNo());
        reviewDTO.setMemberNo(review.getMember().getMemberNo());
        reviewDTO.setRate(review.getRate());
        reviewDTO.setContent(review.getContent());
        reviewDTO.setCreatedDate(review.getCreatedDate());
        reviewDTO.setModifiedDate(review.getModifiedDate());

        return reviewDTO;
    }

    // 포스트 넘버로 조회
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByPostNo(Long postNo, String sortOrder) {
        // 포스트 번호로 리뷰를 데이터베이스에서 가져오기
        List<Review> reviews = reviewRepository.findByPost_PostNo(postNo);

        // 정렬: 최신순(recent) 또는 오래된 순서(old)
        if ("old".equals(sortOrder)) {
            reviews.sort(Comparator.comparing(Review::getCreatedDate)); // 오래된 순서로 정렬
        } else {
            reviews.sort(Comparator.comparing(Review::getCreatedDate).reversed()); // 최신 순서로 정렬
        }

        // 엔티티를 DTO로 변환
        List<ReviewDTO> reviewDTOs = new ArrayList<>();

        for (Review review : reviews) {
            ReviewDTO reviewDTO = new ReviewDTO();
            reviewDTO.setReviewNo(review.getReviewNo());
            reviewDTO.setMemberNo(review.getMember().getMemberNo());
            reviewDTO.setPostNo(review.getPost().getPostNo());
            reviewDTO.setContent(review.getContent());
            reviewDTO.setRate(review.getRate());
            reviewDTO.setCreatedDate(review.getCreatedDate());
            reviewDTO.setModifiedDate(review.getModifiedDate());

            // postNo 수동으로 설정
            if (review.getPost() != null) {
                reviewDTO.setPostNo(review.getPost().getPostNo());
            }

            reviewDTOs.add(reviewDTO);
        }

        return reviewDTOs;
    }

    // 평균 rate(별점) 구하기
    @Transactional(readOnly = true)
    public Double getAverageRateByPostNo(Long postNo) {
        List<Review> reviews = reviewRepository.findByPost_PostNo(postNo);

        if (reviews.isEmpty()) {
            return 0.0; // 리뷰가 없을 경우 0.0 반환
        }

        double averageRate = reviews.stream()
                .mapToInt(Review::getRate)
                .average()
                .orElse(0.0); // 평균 계산, 리뷰가 없으면 0.0 반환

        return averageRate;
    }


    // 멤버 넘버로 조회
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByMemberNo(Long memberNo) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        List<Review> reviews = reviewRepository.findByMember(member);

        return reviews.stream()
                .map(review -> {
                    ReviewDTO reviewDTO = new ReviewDTO();
                    reviewDTO.setReviewNo(review.getReviewNo());
                    reviewDTO.setMemberNo(review.getMember().getMemberNo());
                    reviewDTO.setPostNo(review.getPost().getPostNo());
                    reviewDTO.setRate(review.getRate());
                    reviewDTO.setContent(review.getContent());
                    reviewDTO.setCreatedDate(review.getCreatedDate());
                    reviewDTO.setModifiedDate(review.getModifiedDate());
                    return reviewDTO;
                })
                .collect(Collectors.toList());
    }

    // 리뷰수정
    @Transactional
    public ReviewDTO updateReview(Long reviewNo, ReviewDTO reviewDTO) {
        Review existingReview = reviewRepository.findById(reviewNo)
                .orElseThrow(() -> new EntityNotFoundException("리뷰가 존재하지 않습니다."));

        // Create a new Review instance using the existing one with updated values
        Review updatedReview = Review.builder()
                .reviewNo(existingReview.getReviewNo()) // Keep the same reviewNo
                .member(existingReview.getMember()) // Keep the same member
                .post(existingReview.getPost()) // Keep the same post
                .rate(reviewDTO.getRate()) // Update the rate
                .content(reviewDTO.getContent()) // Update the content
                .createdDate(existingReview.getCreatedDate()) // Keep the createdDate
                .modifiedDate(LocalDateTime.now()) // Update the modifiedDate
                .build();

        // Save the updated review
        reviewRepository.save(updatedReview);

        // Create a new ReviewDTO for the updated review
        return new ReviewDTO(
                updatedReview.getReviewNo(),
                updatedReview.getMember().getMemberNo(),
                updatedReview.getPost().getPostNo(),
                updatedReview.getRate(),
                updatedReview.getContent(),
                updatedReview.getCreatedDate(),
                updatedReview.getModifiedDate()
        );
    }



    @Transactional
    public void deleteReview(Long reviewNo) {
        // Fetch the review to be deleted
        Review review = reviewRepository.findById(reviewNo)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewNo));

        // Deduct points from the member
        Member member = review.getMember(); // Get the associated member
        Point pointDeduction = Point.builder()
                .member(member)
                .point(-10) // Deducting 10 points
                .createdDate(LocalDateTime.now())
                .comment("Points deducted due to review deletion") // Optional comment
                .build();

        // Save the point deduction
        pointRepository.save(pointDeduction);

        // Delete the review
        reviewRepository.delete(review);
        log.info("Review deleted: {}. Points deducted from member: {}", reviewNo, member.getMemberNo());
    }

    // 엔티티 리스트를 DTO 리스트로 변환하는 메서드
    private List<ReviewDTO> convertToDto(List<Review> reviewList) {
        List<ReviewDTO> reviewDTOList = new ArrayList<>();

        for (Review review : reviewList) {
            ReviewDTO dto = new ReviewDTO();
            dto.setReviewNo(review.getReviewNo());
            dto.setPostNo(review.getPost().getPostNo());
            dto.setMemberNo(review.getMember().getMemberNo());
            dto.setRate(review.getRate());
            dto.setContent(review.getContent());
            dto.setCreatedDate(review.getCreatedDate());
            dto.setModifiedDate(review.getModifiedDate());

            reviewDTOList.add(dto);
        }

        return reviewDTOList;
    }

}