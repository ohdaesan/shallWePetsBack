package com.ohdaesan.shallwepets.images.domain.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
@ToString
public class ImagesDTO {
    private Long imageNo;
    private String imageUrl;
    private String imageOrigName;
    private String imageSavedName;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

//    private MultipartFile file;
}
