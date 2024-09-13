package com.ohdaesan.shallwepets.review.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReviewDTO {
    private Long reviewNo;
    private Long memberNo;
    private Long postNo;
    private int rate;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
