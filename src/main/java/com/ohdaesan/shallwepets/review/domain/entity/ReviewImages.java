package com.ohdaesan.shallwepets.review.domain.entity;

import com.ohdaesan.shallwepets.images.domain.entity.Images;
import com.ohdaesan.shallwepets.post.domain.entity.Post;
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
    private Long reviewImageNo;

    @ManyToOne
    @JoinColumn(name = "review_no", nullable = false)
    private Review review;

    @ManyToOne
    @JoinColumn(name = "image_no", nullable = false)
    private Images image;
}
