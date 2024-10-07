package com.ohdaesan.shallwepets.review.service;

import com.ohdaesan.shallwepets.member.domain.dto.MemberDTO;
import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.member.repository.MemberRepository;
import com.ohdaesan.shallwepets.point.domain.entity.Point;
import com.ohdaesan.shallwepets.point.repository.PointRepository;
import com.ohdaesan.shallwepets.point.service.PointService;
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

    private final PointService pointService;

    @Transactional
    public Long createReview(ReviewDTO reviewDTO) {
        // Member와 Post는 외부에서 매핑된 엔티티이므로, ID를 통해 직접 조회해 설정해줍니다.
        Member member = memberRepository.findById(reviewDTO.getMemberNo())
                .orElseThrow(() -> new IllegalArgumentException("해당 번호의 회원이 존재하지 않습니다."));

        Post post = postRepository.findById(reviewDTO.getPostNo())
                .orElseThrow(() -> new IllegalArgumentException("해당 번호의 장소가 존재하지 않습니다."));

        // Review 엔티티 빌더 패턴으로 생성
        Review review = Review.builder()
                .member(member)        // 조회된 Member 엔티티 설정
                .post(post)            // 조회된 Post 엔티티 설정
                .rate(reviewDTO.getRate())      // DTO에서 가져온 평점 설정
                .content(reviewDTO.getContent()) // DTO에서 가져온 리뷰 내용 설정
                .createdDate(LocalDateTime.now()) // 현재 시간으로 생성 시간 설정
                .modifiedDate(null)     // 처음 생성 시 수정 날짜는 null
                .build();

        Review review1 = reviewRepository.save(review);

        // Review 엔티티를 데이터베이스에 저장
        Point point = Point.builder()
                .member(review.getMember())
                .point(10)
                .createdDate(LocalDateTime.now())
                .comment("리뷰 적립")
                .build();

        pointRepository.save(point);

        // 등급 업그레이드 대상인지 확인 후 등급 업데이트
        updateMemberGrade(review, member);

        return review1.getReviewNo();
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
                .orElseThrow(() -> new EntityNotFoundException("번호에 해당하는 리뷰가 존재하지 않습니다."));

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

        // 수정된 내용으로 리뷰 빌드
        Review updatedReview = Review.builder()
                .reviewNo(existingReview.getReviewNo())
                .member(existingReview.getMember())
                .post(existingReview.getPost())
                .rate(reviewDTO.getRate())
                .content(reviewDTO.getContent())
                .createdDate(existingReview.getCreatedDate())
                .modifiedDate(LocalDateTime.now())
                .build();

        // 업데이트 된 리뷰 저장
        reviewRepository.save(updatedReview);

        // ReviewDTO 생성
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
        // 리뷰 넘버에 해당하는 리뷰 가져오기
        Review review = reviewRepository.findById(reviewNo)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewNo));

        // 특정 멤버의 포인트 적립 취소
        Member member = review.getMember();
        Point pointDeduction = Point.builder()
                .member(member)
                .point(-10)
                .createdDate(LocalDateTime.now())
                .comment("리뷰 삭제 적립 취소")
                .build();

        // 포인트 저장
        pointRepository.save(pointDeduction);

        // 리뷰 삭제
        reviewRepository.delete(review);

        // 등급 다운그레이드 대상인지 확인 후 등급 업데이트
        updateMemberGrade(review, member);
    }

    private void updateMemberGrade(Review review, Member member) {
        int totalPoints = pointService.getTotalPoints(review.getMember().getMemberNo());
        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);

        if (totalPoints >= 200 && totalPoints < 500) {
            memberDTO.setGrade("웰시코기");

            Member memberUpdated = modelMapper.map(memberDTO, Member.class);
            memberRepository.save(memberUpdated);
        } else if (totalPoints >= 500 && totalPoints < 1000) {
            memberDTO.setGrade("리트리버");

            Member memberUpdated = modelMapper.map(memberDTO, Member.class);
            memberRepository.save(memberUpdated);
        } else if (totalPoints >= 1000) {
            memberDTO.setGrade("그레이트데인");

            Member memberUpdated = modelMapper.map(memberDTO, Member.class);
            memberRepository.save(memberUpdated);
        }
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

    // 특정 포스트에 대한 리뷰 수 가져오기
    public int getReviewCountByPostNo(Long postNo) {
        return reviewRepository.countByPostPostNo(postNo);
    }

    // 특정 포스트에 대한 평균 평점 가져오기
    public double calculateAverageRateByPostNo(Long postNo) {
        Double averageRate = reviewRepository.findAverageRateByPostNo(postNo);
        return averageRate != null ? averageRate : 0.0; // null 처리
    }


    public List<Review> getReviewsForPost(Long postNo) {
        return reviewRepository.findByPostPostNo(postNo);
    }

}