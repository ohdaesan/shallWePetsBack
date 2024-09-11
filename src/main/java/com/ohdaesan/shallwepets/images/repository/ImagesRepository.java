package com.ohdaesan.shallwepets.images.repository;

import com.ohdaesan.shallwepets.images.domain.entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Integer> {
}
