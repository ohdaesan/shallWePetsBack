package com.ohdaesan.shallwepets.member.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class MemberDTO {
    private Long memberNo;
    private String memberId;
    private String memberPwd;
    private String memberNickname;
    private String memberName;
    private String memberEmail;
    private String memberPhone;
    private String memberDob;
    private String memberZipcode;
    private String memberRoadAddress;
    private String memberDetailAddress;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String status = "ACTIVATED";
    private String memberRole = "USER";
    private String grade = "치와와";
    private Long imageNo;
    private boolean hasBusinessRegistered; // 추가
}
