package com.ohdaesan.shallwepets.post.repository;

import com.ohdaesan.shallwepets.post.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
//    List<Post> findByCtgryTwoNmAndCtyprvnNmContains(String ctgryTwoNm, String ctyprvnNm);

    // ctyprvnNm이 하나일 때
    @Query("SELECT p FROM Post p WHERE p.ctgryTwoNm = :ctgryTwoNm AND " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm, '%') AND " +
            "p.status = 'APPROVED'" +
            "ORDER BY p.fcltyNm")
    Page<Post> findByCtgryTwoNmAndCtyprvnNmContainsOrderByFcltyNm(
            String ctgryTwoNm,
            String ctyprvnNm,
            Pageable pageable);

    // ctyprvnNm이 두 개일 때
    @Query("SELECT p FROM Post p WHERE p.ctgryTwoNm = :ctgryTwoNm AND (" +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm1, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm2, '%')) AND " +
            "p.status = 'APPROVED'" +
            "ORDER BY p.fcltyNm")
    Page<Post> findByCtgryTwoNmAndCtyprvnNm1Or2ContainsOrderByFcltyNm(
            String ctgryTwoNm,
            String ctyprvnNm1,
            String ctyprvnNm2,
            Pageable pageable);

    // ctyprvnNm이 세 개일 때
    @Query("SELECT p FROM Post p WHERE p.ctgryTwoNm = :ctgryTwoNm AND (" +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm1, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm2, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm3, '%')) AND " +
            "p.status = 'APPROVED'" +
            "ORDER BY p.fcltyNm")
    Page<Post> findByCtgryTwoNmAndCtyprvnNm1Or2Or3ContainsOrderByFcltyNm(
            String ctgryTwoNm,
            String ctyprvnNm1,
            String ctyprvnNm2,
            String ctyprvnNm3,
            Pageable pageable);

    // ctyprvnNm이 네 개일 때
    @Query("SELECT p FROM Post p WHERE p.ctgryTwoNm = :ctgryTwoNm AND (" +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm1, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm2, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm3, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm4, '%')) AND " +
            "p.status = 'APPROVED'" +
            "ORDER BY p.fcltyNm")
    Page<Post> findByCtgryTwoNmAndCtyprvnNm1Or2Or3Or4ContainsOrderByFcltyNm(
            String ctgryTwoNm,
            String ctyprvnNm1,
            String ctyprvnNm2,
            String ctyprvnNm3,
            String ctyprvnNm4,
            Pageable pageable);

    // ctyprvnNm이 하나일 때 + 시군구 필터링
    @Query("SELECT p FROM Post p WHERE p.ctgryTwoNm = :ctgryTwoNm AND " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm, '%') AND " +
            "p.signguNm = :signguNm AND " +
            "p.status = 'APPROVED'" +
            "ORDER BY p.fcltyNm")
    Page<Post> findByCtgryTwoNmAndCtyprvnNmContainsAndSignguNmEqualsOrderByFcltyNm(
            String ctgryTwoNm,
            String ctyprvnNm,
            String signguNm,
            Pageable pageable);

    // ctyprvnNm이 두 개일 때 + 시군구 필터링
    @Query("SELECT p FROM Post p WHERE p.ctgryTwoNm = :ctgryTwoNm AND (" +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm1, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm2, '%')) AND " +
            "p.signguNm = :signguNm AND " +
            "p.status = 'APPROVED'" +
            "ORDER BY p.fcltyNm")
    Page<Post> findByCtgryTwoNmAndCtyprvnNm1Or2ContainsAndSignguNmEqualsOrderByFcltyNm(
            String ctgryTwoNm,
            String ctyprvnNm1,
            String ctyprvnNm2,
            String signguNm,
            Pageable pageable);

    // ctyprvnNm이 세 개일 때 + 시군구 필터링
    @Query("SELECT p FROM Post p WHERE p.ctgryTwoNm = :ctgryTwoNm AND (" +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm1, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm2, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm3, '%')) AND " +
            "p.signguNm = :signguNm AND " +
            "p.status = 'APPROVED'" +
            "ORDER BY p.fcltyNm")
    Page<Post> findByCtgryTwoNmAndCtyprvnNm1Or2Or3ContainsAndSignguNmEqualsOrderByFcltyNm(
            String ctgryTwoNm,
            String ctyprvnNm1,
            String ctyprvnNm2,
            String ctyprvnNm3,
            String signguNm,
            Pageable pageable);

    // ctyprvnNm이 네 개일 때 + 시군구 필터링
    @Query("SELECT p FROM Post p WHERE p.ctgryTwoNm = :ctgryTwoNm AND (" +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm1, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm2, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm3, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm4, '%')) AND " +
            "p.signguNm = :signguNm AND " +
            "p.status = 'APPROVED'" +
            "ORDER BY p.fcltyNm")
    Page<Post> findByCtgryTwoNmAndCtyprvnNm1Or2Or3Or4ContainsAndSignguNmEqualsOrderByFcltyNm(
            String ctgryTwoNm,
            String ctyprvnNm1,
            String ctyprvnNm2,
            String ctyprvnNm3,
            String ctyprvnNm4,
            String signguNm,
            Pageable pageable);

    // ctyprvnNm이 하나일 때 + 검색어 필터링
    @Query("SELECT p FROM Post p WHERE p.ctgryTwoNm = :ctgryTwoNm AND " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm, '%') AND " +
            "p.status = 'APPROVED' AND " +
            "p.fcltyNm LIKE CONCAT('%', :keyword, '%') " +
            "ORDER BY p.fcltyNm")
    Page<Post> findByCtgryTwoNmAndCtyprvnNmContainsAndFcltyNmContainsOrderByFcltyNm(
            String ctgryTwoNm,
            String ctyprvnNm,
            String keyword,
            Pageable pageable);

    // ctyprvnNm이 두 개일 때 + 검색어 필터링
    @Query("SELECT p FROM Post p WHERE p.ctgryTwoNm = :ctgryTwoNm AND (" +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm1, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm2, '%')) AND " +
            "p.status = 'APPROVED' AND " +
            "p.fcltyNm LIKE CONCAT('%', :keyword, '%') " +
            "ORDER BY p.fcltyNm")
    Page<Post> findByCtgryTwoNmAndCtyprvnNm1Or2ContainsAndFcltyNmContainsOrderByFcltyNm(
            String ctgryTwoNm,
            String ctyprvnNm1,
            String ctyprvnNm2,
            String keyword,
            Pageable pageable);

    // ctyprvnNm이 세 개일 때 + 검색어 필터링
    @Query("SELECT p FROM Post p WHERE p.ctgryTwoNm = :ctgryTwoNm AND (" +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm1, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm2, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm3, '%')) AND " +
            "p.status = 'APPROVED' AND " +
            "p.fcltyNm LIKE CONCAT('%', :keyword, '%') " +
            "ORDER BY p.fcltyNm")
    Page<Post> findByCtgryTwoNmAndCtyprvnNm1Or2Or3ContainsAndFcltyNmContainsOrderByFcltyNm(
            String ctgryTwoNm,
            String ctyprvnNm1,
            String ctyprvnNm2,
            String ctyprvnNm3,
            String keyword,
            Pageable pageable);

    // ctyprvnNm이 네 개일 때 + 검색어 필터링
    @Query("SELECT p FROM Post p WHERE p.ctgryTwoNm = :ctgryTwoNm AND (" +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm1, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm2, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm3, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm4, '%')) AND " +
            "p.status = 'APPROVED' AND " +
            "p.fcltyNm LIKE CONCAT('%', :keyword, '%') " +
            "ORDER BY p.fcltyNm")
    Page<Post> findByCtgryTwoNmAndCtyprvnNm1Or2Or3Or4ContainsAndFcltyNmContainsOrderByFcltyNm(
            String ctgryTwoNm,
            String ctyprvnNm1,
            String ctyprvnNm2,
            String ctyprvnNm3,
            String ctyprvnNm4,
            String keyword,
            Pageable pageable);

    // ctyprvnNm이 하나일 때 + 시군구 필터링 + 검색어 필터링
    @Query("SELECT p FROM Post p WHERE p.ctgryTwoNm = :ctgryTwoNm AND " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm, '%') AND " +
            "p.status = 'APPROVED' AND " +
            "p.signguNm = :signguNm AND " +
            "p.fcltyNm LIKE CONCAT('%', :keyword, '%') " +
            "ORDER BY p.fcltyNm")
    Page<Post> findByCtgryTwoNmAndCtyprvnNmContainsAndSignguNmEqualsAndFcltyNmContainsOrderByFcltyNm(
            String ctgryTwoNm,
            String ctyprvnNm,
            String signguNm,
            String keyword,
            Pageable pageable);

    // ctyprvnNm이 두 개일 때 + 시군구 필터링 + 검색어 필터링
    @Query("SELECT p FROM Post p WHERE p.ctgryTwoNm = :ctgryTwoNm AND (" +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm1, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm2, '%')) AND " +
            "p.status = 'APPROVED' AND " +
            "p.signguNm = :signguNm AND " +
            "p.fcltyNm LIKE CONCAT('%', :keyword, '%') " +
            "ORDER BY p.fcltyNm")
    Page<Post> findByCtgryTwoNmAndCtyprvnNm1Or2ContainsAndSignguNmEqualsAndFcltyNmContainsOrderByFcltyNm(
            String ctgryTwoNm,
            String ctyprvnNm1,
            String ctyprvnNm2,
            String signguNm,
            String keyword,
            Pageable pageable);

    // ctyprvnNm이 세 개일 때 + 시군구 필터링 + 검색어 필터링
    @Query("SELECT p FROM Post p WHERE p.ctgryTwoNm = :ctgryTwoNm AND (" +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm1, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm2, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm3, '%')) AND " +
            "p.status = 'APPROVED' AND " +
            "p.signguNm = :signguNm AND " +
            "p.fcltyNm LIKE CONCAT('%', :keyword, '%') " +
            "ORDER BY p.fcltyNm")
    Page<Post> findByCtgryTwoNmAndCtyprvnNm1Or2Or3ContainsAndSignguNmEqualsAndFcltyNmContainsOrderByFcltyNm(
            String ctgryTwoNm,
            String ctyprvnNm1,
            String ctyprvnNm2,
            String ctyprvnNm3,
            String signguNm,
            String keyword,
            Pageable pageable);

    // ctyprvnNm이 네 개일 때 + 시군구 필터링 + 검색어 필터링
    @Query("SELECT p FROM Post p WHERE p.ctgryTwoNm = :ctgryTwoNm AND (" +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm1, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm2, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm3, '%') OR " +
            "p.ctyprvnNm LIKE CONCAT('%', :ctyprvnNm4, '%')) AND " +
            "p.status = 'APPROVED' AND " +
            "p.signguNm = :signguNm AND " +
            "p.fcltyNm LIKE CONCAT('%', :keyword, '%') " +
            "ORDER BY p.fcltyNm")
    Page<Post> findByCtgryTwoNmAndCtyprvnNm1Or2Or3Or4ContainsAndSignguNmEqualsAndFcltyNmContainsOrderByFcltyNm(
            String ctgryTwoNm,
            String ctyprvnNm1,
            String ctyprvnNm2,
            String ctyprvnNm3,
            String ctyprvnNm4,
            String signguNm,
            String keyword,
            Pageable pageable);

    @Query("SELECT DISTINCT p.signguNm FROM Post p WHERE p.ctyprvnNm LIKE CONCAT('%', :city, '%') AND p.ctgryTwoNm = :category")
    List<String> findDistinctSignguByCtyprvnNmContainsAndCtgryTwoNm(@Param("city") String city, @Param("category") String category);
}
