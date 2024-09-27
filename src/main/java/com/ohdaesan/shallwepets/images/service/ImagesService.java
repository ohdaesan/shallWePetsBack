package com.ohdaesan.shallwepets.images.service;

import com.ohdaesan.shallwepets.images.domain.dto.ImagesDTO;
import com.ohdaesan.shallwepets.images.domain.entity.Images;
import com.ohdaesan.shallwepets.images.repository.ImagesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

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

    public Images findImagesByImageNo(Long imageNo) {
        Images image = imagesRepository.findImagesByImageNo(imageNo)
                .orElseThrow(() -> new NoSuchElementException("해당 이름과 번호로 가입한 회원이 존재하지 않습니다."));

        return image;
    }
}
