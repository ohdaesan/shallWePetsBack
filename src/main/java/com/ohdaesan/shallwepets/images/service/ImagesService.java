package com.ohdaesan.shallwepets.images.service;

import com.ohdaesan.shallwepets.images.repository.ImagesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImagesService {
    private final ImagesRepository imagesRepository;


}
