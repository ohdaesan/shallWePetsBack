package com.ohdaesan.shallwepets.point.domain.entity;

import com.ohdaesan.shallwepets.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "point")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointNo;

    @ManyToOne
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;

    private int point;
    private LocalDateTime createdDate;
    private String comment;
}
