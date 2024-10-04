package com.ohdaesan.shallwepets.images.repository;

import com.ohdaesan.shallwepets.images.domain.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Long> {

    Optional<Images> findImagesByImageNo(Long imageNo);

    @Query(value = "SELECT i.* FROM Images i " +
            "JOIN PostImages pi ON i.image_no = pi.image_no " +
            "WHERE pi.post_no = :postNo " +
            "UNION " +
            "SELECT i.* FROM Images i " +
            "JOIN ReviewImages ri ON i.image_no = ri.image_no " +
            "JOIN Review r ON ri.review_no = r.review_no " +
            "WHERE r.post_no = :postNo " +
            "ORDER BY i.created_date DESC", nativeQuery = true)
    List<Images> findImagesByPostNo(@Param("postNo") Long postNo);
}
