package com.ohdaesan.shallwepets.post.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PostDTO {
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
    private String petInfoCn;
    private String lnmAddr;
    private String telNo;
    private String hmpgUrl;
    private String rstdeGuidCn;
    private String detailAddress;
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
    private Long memberNo;
    private LocalDateTime createdDate;
    private String status = "AWAITING";
    private String statusExplanation;
    private int viewCount;
}
