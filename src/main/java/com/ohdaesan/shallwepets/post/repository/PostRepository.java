package com.ohdaesan.shallwepets.post.repository;

import com.ohdaesan.shallwepets.post.domain.dto.PostDTO;
import com.ohdaesan.shallwepets.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCtgryTwoNmAndCtyprvnNmIn(String ctgryTwoNm, List<String> ctyprvnNms);
}
