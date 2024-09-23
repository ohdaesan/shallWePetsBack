package com.ohdaesan.shallwepets.post.domain.entity;

import com.ohdaesan.shallwepets.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postNo;

    @ManyToOne
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;

    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    private Status status = Status.AWAITING;    // post가 추가될 때 '승인대기' 상태로 올리기

    private String fcltyNm;
    private String ctgryTwoNm;
    private String ctgryThreeNm;
    private String ctyprvnNm;
    private String signguNm;
    private String legalDongNm;
    private String liNm;
    private String lnbrNm;
    private String roadNm;
    private String buldNo;
    private String lcLa;
    private String lcLo;
    private String zipNo;
    private String rdnmadrNm;
    private String lnmAddr;
    private String telNo;
    private String hmpgUrl;
    private String rstdeGuidCn;
    private String operTime;
    private String parkngPosblAt;
    private String utilizaPrcCn;
    private String petPosblAt;
    private String entrnPosblPetSizeValue;
    private String petLmttMtrCn;
    private String inPlaceAcpPosblAt;
    private String outPlaceAcpPosblAt;
    private String fcltyInfoDc;
    private String petAcpAditChrgeValue;
    private int viewCount;
    private String statusExplanation;
}
