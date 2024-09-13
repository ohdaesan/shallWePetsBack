package com.ohdaesan.shallwepets.post.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PostDTO {
    private Long postNo;
    private Long memberNo;
    private LocalDateTime createdDate;
    private String status = "AWAITING";
    private String fcltyNm;
    private String ctgryTwoNm;
    private String ctgryThreeNm;
    private String ctyprnNm;
    private String signguNm;
    private String legalDongNm;
    private String liNm;
    private String lnbrNm;
    private String roadNm;
    private String buldNo;
    private Double lcLa;
    private Double lcLo;
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
