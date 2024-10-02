package com.ohdaesan.shallwepets.post.domain.entity;

import com.ohdaesan.shallwepets.images.domain.entity.Images;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_images")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PostImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postImageNo;

    @ManyToOne
    @JoinColumn(name = "post_no", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "image_no", nullable = false)
    private Images image;
}
