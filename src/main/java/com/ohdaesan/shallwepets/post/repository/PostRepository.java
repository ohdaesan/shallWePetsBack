package com.ohdaesan.shallwepets.post.repository;

import com.ohdaesan.shallwepets.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCtgryTwoNmAndCtyprvnNmContains(String ctgryTwoNm, String ctyprvnNm);

    @Query("SELECT DISTINCT p.signguNm FROM Post p WHERE p.ctyprvnNm LIKE CONCAT('%', :city, '%') AND p.ctgryTwoNm = :category")
    List<String> findDistinctSignguByCtyprvnNmContainsAndCtgryTwoNm(@Param("city") String city, @Param("category") String category);
}
