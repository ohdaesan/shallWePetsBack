package com.ohdaesan.shallwepets.post.repository;

import com.ohdaesan.shallwepets.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
}
