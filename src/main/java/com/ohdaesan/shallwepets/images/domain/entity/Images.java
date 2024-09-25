package com.ohdaesan.shallwepets.images.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images")
@NoArgsConstructor
//@AllArgsConstructor
@Getter
//@Builder
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageNo;

    private String imageUrl;
    private String imageOrigName;
    private String imageSavedName;

    public Images(String imageUrl, String imageOrigName, String imageSavedName) {
        this.imageUrl = imageUrl;
        this.imageOrigName = imageOrigName;
        this.imageSavedName = imageSavedName;
    }
}
