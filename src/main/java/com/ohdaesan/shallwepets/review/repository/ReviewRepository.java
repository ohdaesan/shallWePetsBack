package com.ohdaesan.shallwepets.review.repository;

import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.post.domain.entity.Post;
import com.ohdaesan.shallwepets.review.domain.dto.ReviewDTO;
import com.ohdaesan.shallwepets.review.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Fetch reviews by Post entity
    List<Review> findByMember(Member member);


    List<Review> findByPost_PostNo(Long postNo);

    int countByPostPostNo(Long postNo); // 특정 포스트에 대한 리뷰 수

    @Query("SELECT AVG(r.rate) FROM Review r WHERE r.post.postNo = :postNo")
    Double findAverageRateByPostNo(@Param("postNo") Long postNo);

    List<Review> findByPostPostNo(Long postNo);
}
