package com.ohdaesan.shallwepets.review.domain.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReviewUpdateRequestDTO {
    private ReviewDTO reviewDTO;
    private List<Long> imagesToRemove;
}
