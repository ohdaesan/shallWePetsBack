package com.ohdaesan.shallwepets.point.repository;

import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.point.domain.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PointRepository  extends JpaRepository<Point, Long> {

    List<Point> findByMember(Member member);

}
