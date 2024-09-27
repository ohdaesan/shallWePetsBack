package com.ohdaesan.shallwepets.images.repository;

import com.ohdaesan.shallwepets.images.domain.entity.Images;
import com.ohdaesan.shallwepets.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Long> {
    Optional<Images> findImagesByImageNo(Long ImageNo);
}
