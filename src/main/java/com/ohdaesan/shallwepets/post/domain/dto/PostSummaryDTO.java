package com.ohdaesan.shallwepets.post.domain.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PostSummaryDTO {
    private PostDTO postDTO;
    private int reviewCount;
    private double averageRate;
}
