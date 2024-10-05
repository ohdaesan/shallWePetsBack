package com.ohdaesan.shallwepets.images.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "images")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageNo;

    private String imageUrl;
    private String imageOrigName;
    private String imageSavedName;
    private String imageSavedPath;

    // localhost에서 실행할 경우 이미지 파일 경로를 저장할 imageSavedPath와 같은 필드가 필요합니다.
    // 이 필드는 이미지를 서버의 특정 디렉토리에 저장한 후 그 저장된 경로를 데이터베이스에 저장하여 나중에 이미지를 불러오거나 관리하는 데 사용됩니다.

    public Images(String imageUrl, String imageOrigName, String imageSavedName) {
        this.imageUrl = imageUrl;
        this.imageOrigName = imageOrigName;
        this.imageSavedName = imageSavedName;
    }
}
