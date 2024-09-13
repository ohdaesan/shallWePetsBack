package com.ohdaesan.shallwepets.member.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
    private String memberAddress;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String status = "ACTIVATED";
    private String memberRole = "USER";
    private String grade;
    private Long imageNo;
}
