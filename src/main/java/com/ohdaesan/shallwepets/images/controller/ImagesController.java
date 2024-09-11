package com.ohdaesan.shallwepets.images.controller;

import com.ohdaesan.shallwepets.images.service.ImagesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Amazon S3 사용
@Tag(name = "Images")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/images")
public class ImagesController {
    private final ImagesService imagesService;


}
