package com.ohdaesan.shallwepets.member.domain.entity;

import com.ohdaesan.shallwepets.images.domain.entity.Images;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Enumerated(EnumType.STRING)
    private Grade grade = Grade.치와와;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVATED;

    @Enumerated(EnumType.STRING)
    private RoleType memberRole = RoleType.USER;

    @OneToOne
    @JoinColumn(name = "image_no")
    private Images image;

    public Long getId() {
        return this.memberNo;
    }
}
