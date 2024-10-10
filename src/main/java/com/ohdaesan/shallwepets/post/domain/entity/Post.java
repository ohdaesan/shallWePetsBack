package com.ohdaesan.shallwepets.post.domain.entity;

import com.ohdaesan.shallwepets.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@NoArgsConstructor
@AllArgsConstructor
@Setter
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
    private Status status = Status.AWAITING;

    private String fcltyNm;
    private String ctyprvnNm;
    private String ctgryTwoNm;
    private String ctgryThreeNm;
    private String signguNm;
    private String legalDongNm;
    private String liNm;
    private String lnbrNm;
    private String roadNm;
    private String buldNo;
    private String lcLa;
    private String lcLo;
    private String telNo;
    private String hmpgUrl;
    private String rstdeGuidCn;
    private String operTime;
    private String parkngPosblAt;
    private String utilizaPrcCn;
    private String rdnmadrNm;
    private String lnmAddr;
    private String zipNo;
    private String petPosblAt;
    private String entrnPosblPetSizeValue;
    private String petLmttMtrCn;
    private String inPlaceAcpPosblAt;
    private String outPlaceAcpPosblAt;
    private String fcltyInfoDc;
    private String petAcpAditChrgeValue;
    private int viewCount;
    private String statusExplanation;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImages> postImages = new ArrayList<>();

    public void addPostImage(PostImages postImage) {
        this.postImages.add(postImage);
        postImage.setPost(this);
    }

    public void removePostImage(PostImages postImage) {
        this.postImages.remove(postImage);
        postImage.setPost(null);
    }

}

