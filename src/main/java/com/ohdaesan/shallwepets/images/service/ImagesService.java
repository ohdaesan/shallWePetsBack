package com.ohdaesan.shallwepets.images.service;

import com.ohdaesan.shallwepets.images.domain.dto.ImagesDTO;
import com.ohdaesan.shallwepets.images.domain.entity.Images;
import com.ohdaesan.shallwepets.images.repository.ImagesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImagesService {
    private final ImagesRepository imagesRepository;

    public Images save(ImagesDTO imagesDTO) {
        Images image = new Images(imagesDTO.getImageUrl(), imagesDTO.getImageOrigName(), imagesDTO.getImageSavedName());
        imagesRepository.save(image);
        return image;
    }
}
