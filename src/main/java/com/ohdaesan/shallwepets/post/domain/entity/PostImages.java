package com.ohdaesan.shallwepets.post.domain.entity;

import com.ohdaesan.shallwepets.images.domain.entity.Images;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_images")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postImageNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_no")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_no")
    private Images image;
}
