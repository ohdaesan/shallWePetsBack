package com.ohdaesan.shallwepets.post.repository;

import com.ohdaesan.shallwepets.post.domain.entity.Post;
import com.ohdaesan.shallwepets.post.domain.entity.PostImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImagesRepository extends JpaRepository<PostImages, Long> {
    List<PostImages> findByImage_ImageNo(Long imageNo);

    void deleteByPost(Post post);
}
