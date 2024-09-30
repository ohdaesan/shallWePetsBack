package com.ohdaesan.shallwepets.review.repository;

import com.ohdaesan.shallwepets.review.domain.entity.ReviewImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewImagesRepository extends JpaRepository<ReviewImages, Long> {
    List<ReviewImages> findByImage_ImageNo(Long imageNo);
}
