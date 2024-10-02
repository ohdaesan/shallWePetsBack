package com.ohdaesan.shallwepets.images.controller;

import com.ohdaesan.shallwepets.global.ResponseDTO;
import com.ohdaesan.shallwepets.images.domain.entity.Images;
import com.ohdaesan.shallwepets.images.service.S3Service;
import com.ohdaesan.shallwepets.images.domain.dto.ImagesDTO;
import com.ohdaesan.shallwepets.images.service.ImagesService;
import com.ohdaesan.shallwepets.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

// Amazon S3 사용
@Tag(name = "Images")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/images")
public class ImagesController {
    private final ImagesService imagesService;
    private final MemberService memberService;
    private final S3Service s3Service;

    @Operation(summary = "이미지 파일 업로드", description = "이미지 AWS 서버에 업로드")
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
            imagesDTO.setCreatedDate(LocalDateTime.now());

            Images savedImage = imagesService.save(imagesDTO);

            return ResponseEntity.ok().body(new ResponseDTO(200, "이미지 업로드 성공", savedImage.getImageNo()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "이미지 파일 가져오기", description = "imageNo에 해당하는 이미지를 AWS 서버에서 가져오기")
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

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "이미지 파일 삭제", description = "imageNo에 해당하는 이미지를 AWS 서버에서 삭제")
    @DeleteMapping("/deleteImageByNo")
    public ResponseEntity<ResponseDTO> deleteImageByNo(@RequestBody Map<String, String> params) {
        Long imageNo = Long.valueOf(params.get("imageNo"));

        try {
            Images image = imagesService.findImagesByImageNo(imageNo);

            // 삭제하려는 이미지를 참조하는 reference 제거
            imagesService.clearImageReferences(imageNo);

            // S3에서 삭제
            s3Service.deleteFile(image.getImageSavedName());

            // DB에서도 삭제
            imagesService.delete(imageNo);

            Map<String, String> response = new HashMap<>();
            response.put("success", "이미지 삭제 성공");

            return ResponseEntity.ok().body(new ResponseDTO(200, "이미지 삭제 성공", response));
        } catch (NoSuchElementException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "해당 번호의 이미지를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND, "이미지 삭제 실패", response));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "S3 파일 삭제 실패", null));
        }
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "이미지 파일 수정", description = "imageNo에 해당하는 이미지를 AWS 서버에서 수정")
    @PutMapping("/updateImageByNo")
    public ResponseEntity<ResponseDTO> updateFile(@RequestParam Long imageNo, MultipartFile file) {
        Map<String, Object> map = null;

        try {
            map = s3Service.updateFile(imageNo, file);

            ImagesDTO imagesDTO = new ImagesDTO();

            imagesDTO.setImageNo(imageNo);
            imagesDTO.setImageOrigName(map.get("imageOrigName").toString());
            imagesDTO.setImageSavedName(map.get("imageSavedName").toString());
            imagesDTO.setImageUrl(map.get("imageUrl").toString());
            imagesDTO.setCreatedDate((LocalDateTime) map.get("imageCreatedDate"));
            imagesDTO.setModifiedDate(LocalDateTime.now());

            imagesService.updateImage(imagesDTO);

            return ResponseEntity.ok().body(new ResponseDTO(200, "이미지 수정 성공", map));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "회원 번호로 프로필 이미지 가져오기", description = "회원 번호로 프로필 이미지 가져오기")
    @GetMapping("/getImageByMemberNo/{memberNo}")
    public ResponseEntity<ResponseDTO> getImageByMemberNo(@PathVariable Long memberNo) {
        Long imageNo = memberService.findImageNoById(memberNo);

        if (imageNo == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "사진을 등록하지 않은 사용자입니다.");

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.NOT_FOUND, "사진을 등록하지 않은 사용자입니다.", response));
        }

        Images image = imagesService.findImagesByImageNo(imageNo);

        if (image == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "이미지 찾기에 실패하였습니다.");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND, "이미지 찾기 실패", response));
        }

        Map<String, Images> response = new HashMap<>();
        response.put("image", image);

        return ResponseEntity.ok().body(new ResponseDTO(200, "이미지 조회 성공", response));
    }

    @Operation(summary = "포스트 번호로 관련 이미지들 가져오기", description = "포스트 번호로 관련 이미지들 가져오기")
    @PostMapping("/getImagesByPostNo")
    public ResponseEntity<ResponseDTO> getImagesByPostNo(@RequestBody Map<String, String> params) {
        Long postNo = Long.valueOf(params.get("postNo"));
        int limit = Integer.parseInt(params.get("limit"));

        List<Images> images = imagesService.getImagesByPostNo(postNo);

        Map<String, Object> response = new HashMap<>();

        if (images.isEmpty()) {
            response.put("error", "포스트와 관련된 이미지가 존재하지 않음");

            return ResponseEntity.ok().body(new ResponseDTO(200, "이미지 조회 성공", response));
        } else {
            images.sort(Comparator.comparing(Images::getCreatedDate).reversed());

            if (limit > 0) {    // limit 길이만큼의 이미지 리스트 반환
                List<Images> sizeLimitImages = images.size() > limit ? images.subList(0, limit) : images;
                response.put("imageList", sizeLimitImages);
            } else {            // -1이면 전체 리스트 반환
                response.put("imageList", images);
            }

            return ResponseEntity.ok().body(new ResponseDTO(200, "이미지 조회 성공", response));
        }
    }
}
