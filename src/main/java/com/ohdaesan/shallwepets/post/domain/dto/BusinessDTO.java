package com.ohdaesan.shallwepets.post.domain.dto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BusinessDTO {
    private Long postNo;
    private String fcltyNm;
    private String rdnmadrNm;
    private String telNo;
}