package com.ohdaesan.shallwepets.images.controller;

import com.ohdaesan.shallwepets.global.ResponseDTO;
import com.ohdaesan.shallwepets.images.domain.entity.Images;
import com.ohdaesan.shallwepets.images.service.S3Service;
import com.ohdaesan.shallwepets.images.domain.dto.ImagesDTO;
import com.ohdaesan.shallwepets.images.service.ImagesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

// Amazon S3 사용
@Tag(name = "Images")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/images")
public class ImagesController {
    private final ImagesService imagesService;
    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<ResponseDTO> uploadFile(MultipartFile file) {
        Map<String, String> map = null;

        try {
            map = s3Service.uploadFile(file);

            String imageUrl = map.get("imageUrl");
            String imageOrigName = map.get("imageOrigName");
            String imageSavedName = map.get("imageSavedName");

            ImagesDTO imagesDTO = new ImagesDTO();
            imagesDTO.setImageUrl(imageUrl);
            imagesDTO.setImageOrigName(imageOrigName);
            imagesDTO.setImageSavedName(imageSavedName);

            Images savedImage = imagesService.save(imagesDTO);

            return ResponseEntity.ok().body(new ResponseDTO(200, "이미지 업로드 성공", savedImage.getImageNo()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/findImageByNo")
    public ResponseEntity<ResponseDTO> findImagesByImageNo(@RequestBody Map<String, String> params) {
        Long imageNo = Long.valueOf(params.get("imageNo"));

        try{
            Images image = imagesService.findImagesByImageNo(imageNo);
            Map<String, Images> response = new HashMap<>();
            response.put("image", image);

            return ResponseEntity.ok().body(new ResponseDTO(200, "이미지 찾기 성공", response));
        } catch (NoSuchElementException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "해당 번호의 이미지를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND, "이미지 찾기 실패", response));
        }
    }
}
