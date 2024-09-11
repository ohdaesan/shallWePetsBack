package com.ohdaesan.shallwepets.bookmark.repository;

import com.ohdaesan.shallwepets.bookmark.domain.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
}
