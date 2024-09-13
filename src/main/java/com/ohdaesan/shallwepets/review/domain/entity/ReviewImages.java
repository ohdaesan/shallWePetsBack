package com.ohdaesan.shallwepets.review.domain.entity;

import com.ohdaesan.shallwepets.images.domain.entity.Images;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_images")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReviewImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewNo;

    @ManyToOne
    @JoinColumn(name = "image_no", nullable = false)
    private Images image;
}
