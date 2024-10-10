package com.ohdaesan.shallwepets.review.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ExtendedReviewDTO extends ReviewDTO {
    private List<ReviewImagesDTO> reviewImages;

    public ExtendedReviewDTO(ReviewDTO reviewDTO) {
        super(reviewDTO.getReviewNo(), reviewDTO.getMemberNo(), reviewDTO.getPostNo(),
                reviewDTO.getRate(), reviewDTO.getContent(), reviewDTO.getCreatedDate(),
                reviewDTO.getModifiedDate());
        this.reviewImages = new ArrayList<>();
    }

    public void setReviewImages(List<ReviewImagesDTO> reviewImages) {
        this.reviewImages = reviewImages;
    }

    public List<ReviewImagesDTO> getReviewImages() {
        return reviewImages;
    }
}
