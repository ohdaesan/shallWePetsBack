package com.ohdaesan.shallwepets.images.repository;

import com.ohdaesan.shallwepets.images.domain.entity.Images;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Long> {
    Optional<Images> findImagesByImageNo(Long imageNo);

    @Query("SELECT i FROM Images i " +
            "JOIN PostImages pi ON i.imageNo = pi.image.imageNo " +
            "WHERE pi.post.postNo = :postNo " +
            "UNION " +
            "SELECT i FROM Images i " +
            "JOIN ReviewImages ri ON i.imageNo = ri.image.imageNo " +
            "JOIN Review r ON ri.review.reviewNo = r.reviewNo " +
            "WHERE r.post.postNo = :postNo " +
            "ORDER BY i.createdDate DESC")
    List<Images> findImagesByPostNo(@Param("postNo") Long postNo);

    @Query("SELECT i FROM Images i " +
            "JOIN ReviewImages ri ON i.imageNo = ri.image.imageNo " +
            "JOIN Review r ON ri.review.reviewNo = r.reviewNo " +
            "WHERE r.reviewNo = :reviewNo")
    List<Images> findImagesByReviewNo(@Param("reviewNo") Long reviewNo);

    @Query("SELECT i FROM Images i " +
            "JOIN PostImages pi ON i.imageNo = pi.image.imageNo " +
            "WHERE pi.post.postNo = :postNo " +
            "ORDER BY i.createdDate DESC")
    List<Images> findPostImagesByPostNo(@Param("postNo") Long postNo);

    @Query("SELECT i FROM Images i " +
            "JOIN PostImages pi ON i.imageNo = pi.image.imageNo " +
            "WHERE pi.post.postNo = :postNo " +
            "UNION " +
            "SELECT i FROM Images i " +
            "JOIN ReviewImages ri ON i.imageNo = ri.image.imageNo " +
            "JOIN Review r ON ri.review.reviewNo = r.reviewNo " +
            "WHERE r.post.postNo = :postNo " +
            "ORDER BY i.createdDate DESC")
    Page<Images> findImagesByPostNoAndPageNo(Long postNo, Pageable pageable);
}
