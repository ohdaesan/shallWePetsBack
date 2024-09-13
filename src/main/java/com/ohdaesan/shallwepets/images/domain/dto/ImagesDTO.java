package com.ohdaesan.shallwepets.images.domain.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ImagesDTO {
    private Long imageNo;
    private String imageUrl;
    private String imageName;
    private String imageSavedName;
}
